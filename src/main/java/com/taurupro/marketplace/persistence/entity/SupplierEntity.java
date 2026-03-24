package com.taurupro.marketplace.persistence.entity;

import com.taurupro.marketplace.persistence.model.MediaFile;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "suppliers", indexes = {
        @Index(name = "idx_supplier_email", columnList = "email", unique = true),
        @Index(name = "idx_supplier_nit", columnList = "nit", unique = true),
        @Index(name = "idx_supplier_user", columnList = "userId"),
})
public class SupplierEntity  extends AuditableEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String nit;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(name="user_id",nullable = false)
    private UUID userId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = true,columnDefinition = "jsonb")
    private MediaFile image;

    @Column(name = "legal_name", nullable = false)
    private String legalName;

}
