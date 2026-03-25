package com.taurupro.marketplace.persistence.crud;

import com.taurupro.marketplace.persistence.entity.OrderItemEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.UUID;

public interface CrudOrderItemEntity  extends ListCrudRepository<OrderItemEntity, UUID> {
}
