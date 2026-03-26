package com.taurupro.marketplace.domain.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateBullDto(
        @NotBlank
        String name,
        @NotBlank
        String stud,
        @NotBlank
        String breedId,
        @NotBlank
        String description,
        @NotBlank
        String birthDate
) {
}
