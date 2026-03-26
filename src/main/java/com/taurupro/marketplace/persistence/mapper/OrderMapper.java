package com.taurupro.marketplace.persistence.mapper;
import com.taurupro.marketplace.domain.dto.OrderDto;
import com.taurupro.marketplace.persistence.entity.OrderEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDto toDto(OrderEntity entity);

    @InheritInverseConfiguration
    OrderEntity toEntity(OrderDto dto);
}
