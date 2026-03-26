package com.taurupro.marketplace.persistence.crud;

import com.taurupro.marketplace.persistence.entity.SupplierEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface CrudSupplierEntity extends ListCrudRepository<SupplierEntity, UUID> {
    Optional<SupplierEntity> findByUserId(UUID userId);
    boolean existsByNit(String nit);
    boolean existsByEmail(String email);
}
