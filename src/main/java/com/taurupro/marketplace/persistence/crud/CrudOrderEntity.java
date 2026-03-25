package com.taurupro.marketplace.persistence.crud;

import com.taurupro.marketplace.persistence.entity.OrderEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.UUID;

public interface CrudOrderEntity extends ListCrudRepository<OrderEntity, UUID> {
}
