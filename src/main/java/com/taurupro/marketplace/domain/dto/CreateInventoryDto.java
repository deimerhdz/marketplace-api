package com.taurupro.marketplace.domain.dto;

public record CreateInventoryDto(
        Integer stock,
        Integer minStock
) {
}
