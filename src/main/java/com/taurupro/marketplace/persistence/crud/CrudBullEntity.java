package com.taurupro.marketplace.persistence.crud;

import com.taurupro.marketplace.persistence.entity.BullEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.ListCrudRepository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CrudBullEntity extends ListCrudRepository<BullEntity, UUID> {
    Optional<BullEntity> findByIdAndSupplierId(UUID id, UUID supplierId);

    Iterable<BullEntity> findByBreed_Name(String name);

    Optional<BullEntity> findBySlug(String slug);

    List<BullEntity> findByCreatedAtAfterOrderByCreatedAtDesc(
            LocalDateTime date,
            Pageable pageable
    );
    List<BullEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
