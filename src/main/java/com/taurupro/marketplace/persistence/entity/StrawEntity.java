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
@Table(name = "straws", indexes = {
        @Index(name = "idx_straw_bull", columnList = "bull_id"),
        @Index(name = "uk_straw_bull_type", columnList = "bull_id, type", unique = true),
        @Index(name = "uk_straw_sku", columnList = "sku", unique = true)
})
public class StrawEntity extends AuditableEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "bull_id", nullable = false)
    private UUID bullId;

    @Column(nullable = false)
    private String sku;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StrawType type;

    @Column(nullable = false, columnDefinition = "Decimal(10,2)")
    private BigDecimal price;

    @Column(name = "min_order", nullable = false)
    private Integer minOrder;

    @ManyToOne
    @JoinColumn(name = "bull_id", referencedColumnName = "id", insertable = false, updatable = false)
    private BullEntity bull;

    @OneToOne(mappedBy = "straw")
    private InventoryEntity inventory;

}
