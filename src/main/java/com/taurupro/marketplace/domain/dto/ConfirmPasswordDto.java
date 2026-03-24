package com.taurupro.marketplace.domain.dto;

import jakarta.validation.constraints.NotBlank;

public record ConfirmPasswordDto(
        @NotBlank
        String email,
        @NotBlank
        String newPassword,
        @NotBlank
        String session) {
}
