package com.taurupro.marketplace.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "inventory", indexes = {
        @Index(name = "uk_inventory_straw", columnList = "strawId", unique = true),
        @Index(name = "idx_inventory_available", columnList = "isAvailable"),
        @Index(name = "idx_inventory_available_straw", columnList = "isAvailable, strawId")
})
public class InventoryEntity extends AuditableEntity{
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name="straw_id",nullable = false)
    private UUID strawId;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false)
    private Integer minStock;

    @Column(nullable = false)
    private Boolean isAvailable;

    @OneToOne()
    @JoinColumn(name = "straw_id",referencedColumnName = "id",insertable = false,updatable = false)
    private StrawEntity straw;

}
