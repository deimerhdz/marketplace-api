package com.taurupro.marketplace.domain.dto;

public record CreateSupplierDto(
        String nit,
        String phone,
        String legalName
) {
}
