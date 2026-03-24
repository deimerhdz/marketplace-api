package com.taurupro.marketplace.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BullDto(
        @NotBlank
        String id,
        @NotBlank
        String name,
        @NotBlank
        String stud,
        @NotBlank
        String description,
        @NotBlank
        String birthDate,
        @NotNull
        BreedDto breed
) {
}
