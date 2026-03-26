package com.taurupro.marketplace.persistence.repository;

import com.taurupro.marketplace.domain.dto.OrderItemResponseDto;
import com.taurupro.marketplace.domain.repository.OrderItemRepository;
import com.taurupro.marketplace.persistence.crud.CrudOrderItemEntity;
import com.taurupro.marketplace.persistence.entity.OrderItemEntity;
import com.taurupro.marketplace.persistence.mapper.OrderItemMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderItemEntityRepository implements OrderItemRepository {
    private final CrudOrderItemEntity crudOrderItemEntity;
    private final OrderItemMapper orderItemMapper;

    public OrderItemEntityRepository(CrudOrderItemEntity crudOrderItemEntity, OrderItemMapper orderItemMapper) {
        this.crudOrderItemEntity = crudOrderItemEntity;
        this.orderItemMapper = orderItemMapper;
    }

    @Override
    public List<OrderItemResponseDto> saveAll(List<OrderItemResponseDto> items) {

        List<OrderItemEntity> itemEntities = this.crudOrderItemEntity.saveAll(this.orderItemMapper.toEntities(items));
        return this.orderItemMapper.toDtos(itemEntities);
    }
}
