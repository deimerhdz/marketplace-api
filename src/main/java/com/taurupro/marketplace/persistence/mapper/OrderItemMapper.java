package com.taurupro.marketplace.persistence.mapper;

import com.taurupro.marketplace.domain.dto.OrderItemResponseDto;
import com.taurupro.marketplace.persistence.entity.OrderItemEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    OrderItemResponseDto toDto(OrderItemEntity entity);

    @InheritInverseConfiguration
    OrderItemEntity toEntity(OrderItemResponseDto dto);

    List<OrderItemEntity> toEntities(Iterable<OrderItemResponseDto> dtos);

    List<OrderItemResponseDto> toDtos(Iterable<OrderItemEntity> entities);
}
