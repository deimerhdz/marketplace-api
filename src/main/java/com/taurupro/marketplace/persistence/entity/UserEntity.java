package com.taurupro.marketplace.persistence.entity;

import com.taurupro.marketplace.domain.enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users", indexes = {
        @Index(name = "idx_user_email", columnList = "email", unique = true),
        @Index(name = "idx_user_cognito_id", columnList = "cognitoId", unique = true),
})
public class UserEntity extends AuditableEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID  id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;


    @Column(name="last_name",nullable = false)
    private String lastName;

    @Column(nullable = false)
    private Boolean active;

    @Enumerated(EnumType.STRING)
    @Column( nullable = false)
    private UserRole role;

    @Column(name = "cognito_sub", nullable = false)
    private String cognitoSub;

    @OneToOne(mappedBy = "user")
    private SupplierEntity supplier;

}
