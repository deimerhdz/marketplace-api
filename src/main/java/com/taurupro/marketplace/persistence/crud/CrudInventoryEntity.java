package com.taurupro.marketplace.persistence.crud;

import com.taurupro.marketplace.persistence.entity.InventoryEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.UUID;

public interface CrudInventoryEntity extends ListCrudRepository<InventoryEntity, UUID> {
}
