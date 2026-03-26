package com.taurupro.marketplace.domain.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateBullDto(
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
