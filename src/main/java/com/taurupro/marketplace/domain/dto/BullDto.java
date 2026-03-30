package com.taurupro.marketplace.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import java.util.List;
import java.util.UUID;

public record BullDto(
        @NotBlank
        String id,
        @NotBlank
        String name,
        @NotBlank
        String numRegister,
        @NotBlank
        String description,
        @NotBlank
        String birthDate,
        @NotNull
        BreedDto breed,
        @NotNull
        UUID supplierId,
        @Null
        MediaFileDto image,
        @Null
        MediaFileDto video,
        @Null
        MediaFileDto geneticEvaluation,
        @Null
        List<MediaFileDto> gallery

) {
}
