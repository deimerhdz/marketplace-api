package com.taurupro.marketplace.persistence.entity;

import com.taurupro.marketplace.persistence.model.MediaFile;
import com.taurupro.marketplace.persistence.model.Pedigree;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bulls", indexes = {
        @Index(name = "idx_bull_slug", columnList = "slug", unique = true),
        @Index(name = "idx_bull_breed", columnList = "breedId"),
        @Index(name = "idx_bull_supplier", columnList = "supplierId"),
        @Index(name = "idx_bull_name", columnList = "name"),
})
public class BullEntity extends AuditableEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String slug;

    @Column(nullable = false)
    private String stud;

    @Column(name="breed_id" ,nullable = false)
    private UUID breedId;

    @Column(name="supplier_id",nullable = false)
    private UUID supplierId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = true,columnDefinition = "jsonb")
    private MediaFile image;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = true,columnDefinition = "jsonb")
    private MediaFile video;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = true,columnDefinition = "jsonb")
    private Pedigree pedigree;

    @Column(nullable = false)
    private String description;

    @Column(name="birth_date",nullable = false)
    private LocalDate birthDate;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = true,columnDefinition = "jsonb")
    private List<MediaFile> gallery;

    @OneToOne()
    @JoinColumn(name = "breed_id",referencedColumnName = "id",insertable = false,updatable = false)
    private BreedEntity breed;

}
