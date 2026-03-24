package com.taurupro.marketplace.persistence.mapper;

import com.taurupro.marketplace.domain.dto.SupplierDto;
import com.taurupro.marketplace.persistence.entity.SupplierEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SupplierMapper {
    SupplierDto toDto(SupplierEntity entity);

    @InheritInverseConfiguration
    SupplierEntity toEntity(SupplierDto dto);
}
