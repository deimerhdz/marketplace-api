package com.taurupro.marketplace.domain.dto;

import com.taurupro.marketplace.domain.enums.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SignUpUser(
        @NotBlank(message = "El email es obligatorio.")
        String email,
        @NotBlank(message = "El nombre es obligatorio.")
        String name,
        @NotBlank(message = "El apellido es obligatorio.")
        String lastName,
        @NotNull(message = "El rol es obligatorio.")
        UserRole role,

        CreateSupplierDto supplier
) {
}
