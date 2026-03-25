package com.taurupro.marketplace.domain.repository;
import com.taurupro.marketplace.domain.dto.OrderItemResponseDto;

import java.util.List;

public interface OrderItemRepository {

    List<OrderItemResponseDto> saveAll(List<OrderItemResponseDto> items );
}
