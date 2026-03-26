package com.taurupro.marketplace.domain.repository;

import com.taurupro.marketplace.domain.dto.OrderDto;

public interface OrderRepository {
    OrderDto save(OrderDto orderDto);
}
