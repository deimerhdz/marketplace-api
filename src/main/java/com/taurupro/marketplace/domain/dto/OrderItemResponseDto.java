package com.taurupro.marketplace.domain.dto;

import com.taurupro.marketplace.domain.enums.StrawType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponseDto {
    private UUID id;
    private String sku;
    private String bullName;
    private UUID orderId;
    private StrawType type;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal subtotal;
}
