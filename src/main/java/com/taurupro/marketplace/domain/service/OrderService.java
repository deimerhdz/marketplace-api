package com.taurupro.marketplace.domain.service;

import java.util.*;
import java.math.BigDecimal;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import com.taurupro.marketplace.domain.dto.*;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import com.taurupro.marketplace.domain.enums.OrderStatus;
import com.taurupro.marketplace.domain.enums.IdempotencyStatus;
import org.springframework.transaction.annotation.Transactional;
import com.taurupro.marketplace.domain.repository.OrderRepository;
import com.taurupro.marketplace.domain.repository.StrawRepository;
import com.taurupro.marketplace.persistence.entity.OrderItemEntity;
import com.taurupro.marketplace.persistence.mapper.OrderItemMapper;
import com.taurupro.marketplace.domain.repository.OrderItemRepository;
import com.taurupro.marketplace.persistence.entity.IdempotencyKeyEntity;
import com.taurupro.marketplace.domain.exception.RequestStillProcessingException;


@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private static final String ENDPOINT = "/orders";

    private final OrderRepository orderRepository;
    private final StrawRepository strawRepository;
    private final OrderItemMapper orderItemMapper;
    private final OrderItemRepository orderItemRepository;
    private final IdempotencyService idempotencyService;

    public OrderResponseDto createOrder(
            String idempotencyKey,
            UUID customerId,
            CreateOrderDto request
    ) {
        String requestHash = idempotencyService.hashRequest(request);

        // ── 1. Verificar si ya existe una key para este endpoint ──
        Optional<IdempotencyKeyEntity> existing =
                idempotencyService.findExisting(idempotencyKey, ENDPOINT, requestHash);

        if (existing.isPresent()) {
            return handleExistingKey(existing.get(), idempotencyKey, customerId, request);
        }

        // ── 2. Registrar como PROCESSING (transacción separada) ──
        idempotencyService.markAsProcessing(idempotencyKey, ENDPOINT, requestHash);

        // ── 3. Ejecutar la lógica de negocio ──
        try {
            OrderResponseDto response = processOrder(customerId, request);
            idempotencyService.markAsCompleted(idempotencyKey, IdempotencyStatus.SUCCESS, response, 200);
            return response;
        } catch (Exception e) {
            idempotencyService.markAsCompleted(idempotencyKey, IdempotencyStatus.FAILED,
                    Map.of("error", e.getMessage()), 422);
            throw e;
        }
    }

    private OrderResponseDto handleExistingKey(IdempotencyKeyEntity existing,
                                               String idempotencyKey,
                                               UUID customerId,
                                               CreateOrderDto request) {
        return switch (existing.getStatus()) {
            case SUCCESS ->
                // Devolver respuesta cacheada sin tocar la BD
                    idempotencyService.deserializeResponse(existing, OrderResponseDto.class);

            case PROCESSING ->
                // Otra instancia/hilo está procesando la misma petición
                    throw new RequestStillProcessingException(idempotencyKey);

            case FAILED -> {
                // ✅ Limpiar el intento fallido y reprocesar
                idempotencyService.delete(idempotencyKey);
                idempotencyService.markAsProcessing(idempotencyKey, ENDPOINT,
                        idempotencyService.hashRequest(request));
                yield executeOrder(idempotencyKey, customerId, request);
            }
        };
    }

    private OrderResponseDto executeOrder(String idempotencyKey, UUID customerId, CreateOrderDto request) {
        try {
            OrderResponseDto response = processOrder(customerId, request);
            idempotencyService.markAsCompleted(idempotencyKey, IdempotencyStatus.SUCCESS, response, 200);
            return response;
        } catch (Exception e) {
            idempotencyService.markAsCompleted(idempotencyKey, IdempotencyStatus.FAILED,
                    Map.of("error", e.getMessage()), 422);
            throw e;
        }
    }

    @Transactional
    protected OrderResponseDto processOrder(UUID customerId, CreateOrderDto request) {
        // ── Validar y construir ítems ──
        List<OrderItemResponseDto> items = buildOrderItems(request.items());
        UUID supplierId = extractSupplierId(request.items());
        BigDecimal total = items.stream()
                .map(OrderItemResponseDto::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // ── Crear la orden ──
        OrderDto order = new OrderDto(
                null,
                customerId,
                supplierId,
                total,
                request.shippingAddress(),
                OrderStatus.PENDING,
                null
        );


        OrderDto savedOrder = orderRepository.save(order);

        // ── Persistir ítems ──
        items.forEach(item -> item.setOrderId(savedOrder.id()));
        List<OrderItemResponseDto> savedItems = orderItemRepository.saveAll(items);

        return toOrderResponse(savedOrder, savedItems);
    }

    private List<OrderItemResponseDto> buildOrderItems(List<CreateOrderItemDto> requests) {
        return requests.stream().map(req -> {
            StrawDto straw = this.strawRepository.findById(req.strawId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Straw not found: " + req.strawId()));
            OrderItemEntity item = getOrderItemEntity(req, straw);
            return this.orderItemMapper.toDto(item);
        }).toList();
    }

    @Nonnull
    private static OrderItemEntity getOrderItemEntity(CreateOrderItemDto req, StrawDto straw) {
        if (req.quantity() < straw.minOrder()) {
            throw new IllegalArgumentException(
                    "Minimum order for '" + straw.sku() + "' is " + straw.minOrder());
        }

        BigDecimal subtotal = straw.price()
                .multiply(BigDecimal.valueOf(req.quantity()));

        OrderItemEntity item = new OrderItemEntity();
        item.setSku(straw.sku());
        item.setStrawId(straw.id());
        item.setType(straw.type());
        item.setBullName(straw.bull().name());
        item.setQuantity(req.quantity());
        item.setPrice(straw.price());
        item.setSubtotal(subtotal);
        return item;
    }

    private OrderResponseDto toOrderResponse(OrderDto order, List<OrderItemResponseDto> items) {
        List<OrderItemResponseDto> itemResponses = items.stream()
                .map(i -> new OrderItemResponseDto(
                        i.getId(),
                        i.getSku(),
                        i.getBullName(),
                        i.getOrderId(),
                        i.getType(),
                        i.getQuantity(),
                        i.getPrice(),
                        i.getSubtotal()
                )).toList();

        return new OrderResponseDto(
                order.id(), order.supplierId(), order.customerId(),
                order.total(), order.orderStatus(),
                order.shippingAddress(), itemResponses, order.createdAt()
        );
    }

    private UUID extractSupplierId(List<CreateOrderItemDto> requests) {
        // Obtener todos los supplierIds de los straws
        Set<UUID> supplierIds = requests.stream()
                .map(req -> strawRepository.findById(req.strawId())
                        .orElseThrow(() -> new EntityNotFoundException("Straw not found: " + req.strawId()))
                        .bull()
                        .supplierId())
                .collect(Collectors.toSet());

        // Validar que todos los ítems son del mismo supplier
        if (supplierIds.size() > 1) {
            throw new IllegalArgumentException(
                    "All items must belong to the same supplier. Found: " + supplierIds
            );
        }

        return supplierIds.iterator().next();
    }
}
