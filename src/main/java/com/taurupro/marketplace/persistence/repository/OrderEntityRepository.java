package com.taurupro.marketplace.persistence.repository;

import com.taurupro.marketplace.domain.dto.OrderDto;
import com.taurupro.marketplace.domain.repository.OrderRepository;
import com.taurupro.marketplace.persistence.crud.CrudOrderEntity;
import com.taurupro.marketplace.persistence.entity.OrderEntity;
import com.taurupro.marketplace.persistence.mapper.OrderMapper;
import org.springframework.stereotype.Repository;

@Repository
public class OrderEntityRepository implements OrderRepository {
    CrudOrderEntity crudOrderEntity;
    OrderMapper orderMapper;
    public OrderEntityRepository(CrudOrderEntity crudOrderEntity,   OrderMapper orderMapper) {
        this.crudOrderEntity = crudOrderEntity;
        this.orderMapper=orderMapper;
    }

    @Override
    public OrderDto save(OrderDto orderDto) {
        OrderEntity orderEntity= this.crudOrderEntity.save(this.orderMapper.toEntity(orderDto));
        return this.orderMapper.toDto(orderEntity);
    }
}
