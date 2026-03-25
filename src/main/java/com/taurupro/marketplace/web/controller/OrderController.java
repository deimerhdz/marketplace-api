package com.taurupro.marketplace.web.controller;

import com.taurupro.marketplace.domain.dto.CreateOrderDto;
import com.taurupro.marketplace.domain.dto.OrderResponseDto;
import com.taurupro.marketplace.domain.service.OrderService;
import com.taurupro.marketplace.persistence.model.UserMain;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")

public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('SUPPLIER', 'CUSTOMER')")
    public ResponseEntity<OrderResponseDto> createOrder(
            @RequestHeader("X-Idempotency-Key") String idempotencyKey,
            @AuthenticationPrincipal UserMain userMain,
            @RequestBody @Valid CreateOrderDto request
    ) {
        OrderResponseDto response = orderService.createOrder(
                idempotencyKey,
                userMain.getId(),
                request
        );
        return ResponseEntity.ok(response);
    }
}