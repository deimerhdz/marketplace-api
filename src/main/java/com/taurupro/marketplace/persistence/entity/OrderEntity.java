package com.taurupro.marketplace.persistence.entity;

import com.taurupro.marketplace.domain.enums.OrderStatus;
import com.taurupro.marketplace.persistence.model.ShippingAddress;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders", indexes = {
        @Index(name = "idx_order_customer", columnList = "customerId"),
        @Index(name = "idx_order_supplier", columnList = "supplierId"),
        @Index(name = "idx_order_customer_created", columnList = "customerId, created_at"),
        @Index(name = "idx_order_supplier_status", columnList = "supplierId, orderStatus"),
        @Index(name = "idx_order_status_created", columnList = "orderStatus, created_at")
})
public class OrderEntity{

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name="customer_id" ,nullable = false)
    private UUID customerId;

    @Column(name="supplier_id" ,nullable = false)
    private UUID supplierId;

    @Column(nullable = false,columnDefinition = "Decimal(10,2)")
    private BigDecimal total;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "address", nullable = false,columnDefinition = "jsonb")
    private ShippingAddress shippingAddress;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    protected LocalDateTime createdAt;

}
