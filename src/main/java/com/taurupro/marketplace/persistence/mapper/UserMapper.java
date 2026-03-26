package com.taurupro.marketplace.persistence.mapper;

import com.taurupro.marketplace.domain.dto.BreedDto;
import com.taurupro.marketplace.domain.dto.UserDto;
import com.taurupro.marketplace.persistence.entity.BreedEntity;
import com.taurupro.marketplace.persistence.entity.UserEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(UserEntity entity);

    @InheritInverseConfiguration
    UserEntity toEntity(UserDto dto);
}
