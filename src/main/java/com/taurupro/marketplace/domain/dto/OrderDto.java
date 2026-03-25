package com.taurupro.marketplace.domain.dto;

import com.taurupro.marketplace.domain.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record OrderDto(
        UUID id,
        UUID customerId,
        UUID supplierId,
        BigDecimal total,
        ShippingAddressDto shippingAddress,
        OrderStatus orderStatus,
        LocalDateTime createdAt
) {
}
