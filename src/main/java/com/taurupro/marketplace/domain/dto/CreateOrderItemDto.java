package com.taurupro.marketplace.domain.dto;

import java.util.UUID;

public record CreateOrderItemDto(
        UUID strawId,
        Integer quantity
) {
}
