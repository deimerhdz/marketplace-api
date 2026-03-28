package com.taurupro.marketplace.domain.dto;

import com.taurupro.marketplace.domain.enums.StrawType;

import java.math.BigDecimal;
import java.util.UUID;

public record StrawCatalogDto(
        UUID id,
        StrawType type,
        BigDecimal price,
        Integer minOrder,
        String sku
) {
}
