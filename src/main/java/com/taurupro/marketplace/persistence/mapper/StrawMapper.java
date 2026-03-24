package com.taurupro.marketplace.persistence.mapper;

import com.taurupro.marketplace.domain.dto.*;
import com.taurupro.marketplace.persistence.entity.StrawEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StrawMapper {
    StrawDto toDto(StrawEntity entity);
    List<StrawDto> toDto(Iterable<StrawEntity> entities);
    @InheritInverseConfiguration
    StrawEntity toEntity(StrawDto dto);

    @InheritInverseConfiguration
    StrawEntity toEntity(CreateStrawDto dto);


    void updateEntityFromDto(UpdateStrawDto updateStrawDto , @MappingTarget StrawEntity strawEntity);
}
