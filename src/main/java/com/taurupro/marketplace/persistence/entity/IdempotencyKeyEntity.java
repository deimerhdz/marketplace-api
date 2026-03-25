package com.taurupro.marketplace.persistence.entity;

import com.taurupro.marketplace.domain.enums.IdempotencyStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "idempotency_keys")
public class IdempotencyKeyEntity {

    @Id
    private String key;

    @Column(nullable = false)
    private String endpoint; // /orders, /payments

    @Column(name = "request_hash", nullable = false)
    private String requestHash;

    @Column(name = "response_body", columnDefinition = "TEXT")
    private String responseBody;

    @Column(name = "status_code")
    private Integer statusCode;

    @Enumerated(EnumType.STRING)
    private IdempotencyStatus status; // PROCESSING, SUCCESS, FAILED

    @CreationTimestamp
    private LocalDateTime createdAt;
}