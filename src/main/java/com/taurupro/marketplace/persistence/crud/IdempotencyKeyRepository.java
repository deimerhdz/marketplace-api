package com.taurupro.marketplace.persistence.crud;

import com.taurupro.marketplace.persistence.entity.IdempotencyKeyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IdempotencyKeyRepository extends JpaRepository<IdempotencyKeyEntity, String> {
    Optional<IdempotencyKeyEntity> findByKeyAndEndpoint(String key, String endpoint);
}