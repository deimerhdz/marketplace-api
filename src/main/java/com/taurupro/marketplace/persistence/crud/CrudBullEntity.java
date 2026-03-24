package com.taurupro.marketplace.persistence.crud;

import com.taurupro.marketplace.persistence.entity.BullEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface CrudBullEntity extends ListCrudRepository<BullEntity, UUID> {
    Optional<BullEntity> findByIdAndSupplierId(UUID id, UUID supplierId);
}
