package com.taurupro.marketplace.domain.dto;


import java.util.UUID;

public record InventoryDto(
        UUID id,
        Integer stock,
        Integer minStock,
        Boolean isAvailable
) {
}
