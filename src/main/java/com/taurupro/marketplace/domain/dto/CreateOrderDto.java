package com.taurupro.marketplace.domain.dto;

import java.util.List;

public record CreateOrderDto(
      ShippingAddressDto  shippingAddress,
      List<CreateOrderItemDto> items
) {
}
