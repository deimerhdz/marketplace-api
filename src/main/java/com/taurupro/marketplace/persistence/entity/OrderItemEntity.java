package com.taurupro.marketplace.persistence.entity;

import com.taurupro.marketplace.domain.enums.StrawType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_item", indexes = {
        @Index(name = "idx_order_item_order", columnList = "orderId"),
        @Index(name = "uk_order_item_order_sku", columnList = "orderId, sku", unique = true),
        @Index(name = "idx_order_item_straw", columnList = "strawId"),
        @Index(name = "idx_order_item_sku", columnList = "sku")
})
public class OrderItemEntity {

    @Id
    @GeneratedValue
    private UUID id;
    @Column(nullable = false)
    private String sku;

    @Column(nullable = false)
    private UUID orderId;

    @Column(nullable = false)
    private UUID strawId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StrawType type;

    @Column(nullable = false)
    private String bullName;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false,columnDefinition = "Decimal(10,2)")
    private BigDecimal price;

    @Column(nullable = false,columnDefinition = "Decimal(10,2)")
    private BigDecimal subtotal;
}
