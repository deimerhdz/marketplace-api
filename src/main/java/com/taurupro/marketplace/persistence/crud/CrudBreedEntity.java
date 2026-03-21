package com.taurupro.marketplace.persistence.crud;

import com.taurupro.marketplace.persistence.entity.BreedEntity;
import org.springframework.data.repository.ListCrudRepository;

public interface CrudBreedEntity extends ListCrudRepository<BreedEntity,String> {
}
