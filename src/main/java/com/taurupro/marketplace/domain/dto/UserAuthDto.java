package com.taurupro.marketplace.domain.dto;

import com.taurupro.marketplace.domain.enums.UserRole;

public record UserAuthDto(
        String name,
        String lastName,
        UserRole role
) {
}
