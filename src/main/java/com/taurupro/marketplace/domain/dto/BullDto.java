package com.taurupro.marketplace.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

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
        BreedDto breed,
        @NotNull
        UUID supplierId
) {
}
