package com.taurupro.marketplace.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterSupplierDto(
        @NotBlank @Email
        String email,

        @NotBlank @Size(min = 8)
        String password,

        @NotBlank
        String name,

        @NotBlank
        String lastName,

// Datos del supplier
        @NotBlank
        String nit,

        @NotBlank
        String phone,

        @NotBlank
        String legalName
) {
}
