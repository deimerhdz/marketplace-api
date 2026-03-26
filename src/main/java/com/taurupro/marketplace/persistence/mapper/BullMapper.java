package com.taurupro.marketplace.persistence.mapper;

import com.taurupro.marketplace.domain.dto.*;
import com.taurupro.marketplace.persistence.entity.BreedEntity;
import com.taurupro.marketplace.persistence.entity.BullEntity;
import com.taurupro.marketplace.persistence.entity.StrawEntity;
import com.taurupro.marketplace.persistence.entity.UserEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BullMapper {
    BullDto toDto(BullEntity entity);

    List<CatalogDto> toDto(Iterable<BullEntity> entities);

    CatalogDetailDto toCatalog(BullEntity entity);

    @InheritInverseConfiguration
    BullEntity toEntity(BullDto dto);


    @InheritInverseConfiguration
    BullEntity toEntity(CreateBullDto dto);


    void updateEntityFromDto(UpdateBullDto updateBullDto, @MappingTarget BullEntity bullEntity);
}
