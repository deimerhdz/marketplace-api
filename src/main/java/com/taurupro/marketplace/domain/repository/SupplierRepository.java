package com.taurupro.marketplace.domain.repository;

import com.taurupro.marketplace.domain.dto.CreateSupplierDto;
import com.taurupro.marketplace.domain.dto.SupplierDto;

import java.util.Optional;
import java.util.UUID;

public interface SupplierRepository {
    SupplierDto save(SupplierDto supplier);
    Optional<SupplierDto> findByUserId(UUID userId);
    boolean existsByNit(String nit);
    boolean existsByEmail(String email);
}
