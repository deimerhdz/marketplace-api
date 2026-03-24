package com.taurupro.marketplace.domain.dto;

import com.taurupro.marketplace.domain.enums.StrawType;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateStrawDto(
        UUID bullId,
        StrawType type,
        BigDecimal price,
        Integer minOrder
) {
}
