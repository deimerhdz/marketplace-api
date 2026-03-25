package com.taurupro.marketplace.domain.dto;

import com.taurupro.marketplace.domain.enums.StrawType;

import java.math.BigDecimal;

public record StrawCatalogDto(
        StrawType type,
        BigDecimal price,
        Integer minOrder,
        String sku
) {
}
