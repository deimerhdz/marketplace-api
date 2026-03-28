package com.taurupro.marketplace.domain.dto;

import java.util.UUID;

public record SupplierDto(
        String nit,
        String email,
        String phone,
        String legalName,
        MediaFileDto image,
        UUID userId,
        UUID id
) {
}
