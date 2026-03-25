package com.taurupro.marketplace.domain.dto;

import com.taurupro.marketplace.domain.enums.OrderStatus;
import com.taurupro.marketplace.persistence.model.ShippingAddress;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderResponseDto(
        UUID orderId,
        UUID supplierId,
        UUID customerId,
        BigDecimal total,
        OrderStatus status,
        ShippingAddressDto shippingAddress,
        List<OrderItemResponseDto> items,
        LocalDateTime createdAt) {
}
