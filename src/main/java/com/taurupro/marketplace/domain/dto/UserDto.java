package com.taurupro.marketplace.domain.dto;

import com.taurupro.marketplace.domain.enums.UserRole;

import java.util.UUID;

public record UserDto(
         UUID id,
         String email,
         String name,
         String lastName,
         String cognitoSub,
         UserRole role,
         Boolean active,
         SupplierDto supplier
) {
}
