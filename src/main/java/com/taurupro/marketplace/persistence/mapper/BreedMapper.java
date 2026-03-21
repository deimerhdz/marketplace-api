package com.taurupro.marketplace.persistence.mapper;

import com.taurupro.marketplace.domain.dto.BreedDto;
import com.taurupro.marketplace.persistence.entity.BreedEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BreedMapper {
    List<BreedDto> toDto(Iterable<BreedEntity> entities);
}
