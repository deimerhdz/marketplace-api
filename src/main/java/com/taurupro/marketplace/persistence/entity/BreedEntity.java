package com.taurupro.marketplace.persistence.entity;

import com.taurupro.marketplace.domain.enums.BreedType;
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
@Table(name = "breeds",
        uniqueConstraints = {
        @UniqueConstraint(name = "uk_breed_name", columnNames = {"name", "type"})
        }
)
public class BreedEntity extends AuditableEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BreedType type;

}
