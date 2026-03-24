package com.taurupro.marketplace.domain.dto;

import com.taurupro.marketplace.domain.enums.StrawType;

import java.math.BigDecimal;
import java.util.UUID;

public record StrawDto(
        StrawType type,
        BigDecimal price,
        Integer minOrder,
        InventoryDto inventory
) {
}
