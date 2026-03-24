package com.taurupro.marketplace.persistence.crud;

import com.taurupro.marketplace.persistence.entity.StrawEntity;
import com.taurupro.marketplace.persistence.entity.UserEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CrudStrawEntity extends ListCrudRepository<StrawEntity, UUID> {
    List<StrawEntity> findByBullId(UUID bullId);
}
