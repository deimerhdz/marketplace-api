package com.taurupro.marketplace.persistence.mapper;


import com.taurupro.marketplace.domain.dto.CreateInventoryDto;
import com.taurupro.marketplace.persistence.entity.InventoryEntity;
import com.taurupro.marketplace.domain.dto.InventoryDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InventoryMapper {
    InventoryDto toDto(InventoryEntity entity);

    @InheritInverseConfiguration
    InventoryEntity toEntity(CreateInventoryDto dto);
}
