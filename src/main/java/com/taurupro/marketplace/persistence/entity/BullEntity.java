package com.taurupro.marketplace.persistence.entity;

import com.taurupro.marketplace.persistence.model.MediaFile;
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
        @Index(name = "idx_bull_created_at", columnList = "createdAt")
})
public class BullEntity extends AuditableEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String slug;

    @Column(name = "num_register", nullable = false)
    private String numRegister;

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
    @Column(name = "genetic_evaluation", nullable = true, columnDefinition = "jsonb")
    private MediaFile geneticEvaluation ;

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

    @OneToMany(mappedBy = "bull")
    private List<StrawEntity> straws;

}
