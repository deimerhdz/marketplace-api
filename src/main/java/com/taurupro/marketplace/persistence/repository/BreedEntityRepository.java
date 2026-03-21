package com.taurupro.marketplace.persistence.repository;

import com.taurupro.marketplace.domain.dto.BreedDto;
import com.taurupro.marketplace.domain.repository.BreedRepository;
import com.taurupro.marketplace.persistence.crud.CrudBreedEntity;
import com.taurupro.marketplace.persistence.mapper.BreedMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BreedEntityRepository implements BreedRepository {

    private final CrudBreedEntity crudBreedEntity;

    private final BreedMapper breedMapper;

    public BreedEntityRepository(CrudBreedEntity crudBreedEntity, BreedMapper breedMapper) {
        this.crudBreedEntity = crudBreedEntity;
        this.breedMapper = breedMapper;
    }

    @Override
    public List<BreedDto> getAll() {
        return this.breedMapper.toDto(this.crudBreedEntity.findAll());
    }
}
