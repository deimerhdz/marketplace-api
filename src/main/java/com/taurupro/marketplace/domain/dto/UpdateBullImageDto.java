package com.taurupro.marketplace.domain.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateBullImageDto(
        @NotBlank
         String key,
        @NotBlank
         String contentType
) {
}
