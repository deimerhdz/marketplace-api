package com.taurupro.marketplace.domain.dto;

import com.taurupro.marketplace.domain.enums.BreedType;

public record BreedDto(
        String id,
        String name,
        BreedType type
) {
}
