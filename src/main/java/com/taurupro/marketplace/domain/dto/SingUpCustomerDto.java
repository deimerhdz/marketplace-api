package com.taurupro.marketplace.domain.dto;

import jakarta.validation.constraints.NotBlank;

public record SingUpCustomerDto(
        @NotBlank(message = "El email es obligatorio.")
        String email,
        @NotBlank(message = "El nombre es obligatorio.")
        String name,
        @NotBlank(message = "El apellido es obligatorio.")
        String lastName,
        @NotBlank(message = "La contraseña es obligatoria.")
        String password
) {
}
