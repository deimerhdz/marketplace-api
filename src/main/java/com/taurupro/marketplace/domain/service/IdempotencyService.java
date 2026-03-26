package com.taurupro.marketplace.domain.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taurupro.marketplace.domain.enums.IdempotencyStatus;
import com.taurupro.marketplace.domain.exception.IdempotencyConflictException;
import com.taurupro.marketplace.persistence.crud.IdempotencyKeyRepository;
import com.taurupro.marketplace.persistence.entity.IdempotencyKeyEntity;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.Optional;

@Service
@Slf4j
public class IdempotencyService {
    private final IdempotencyKeyRepository idempotencyKeyRepository;
    private final ObjectMapper objectMapper;


    public IdempotencyService(IdempotencyKeyRepository idempotencyKeyRepository, ObjectMapper objectMapper) {
        this.idempotencyKeyRepository = idempotencyKeyRepository;
        this.objectMapper = objectMapper;
    }

    /**
     * Genera un hash SHA-256 del request para detectar si la key
     * se reutiliza con un payload diferente.
     */
    public String hashRequest(Object request) {
        try {
            String json = objectMapper.writeValueAsString(request);
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(json.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (Exception e) {
            throw new RuntimeException("Error hashing request", e);
        }
    }

    /**
     * Busca una key existente para el endpoint dado.
     * Lanza excepción si el hash no coincide (mismo key, distinto body).
     */
    //todo: usar mapper y devovel un dto
    public Optional<IdempotencyKeyEntity> findExisting(
            String key,
            String endpoint,
            String requestHash
    ) {
        return idempotencyKeyRepository.findByKeyAndEndpoint(key, endpoint)
                .map(existing -> {
                    if (!existing.getRequestHash().equals(requestHash)) {
                        throw new IdempotencyConflictException(
                                "Idempotency key '" + key + "' was used with a different request payload"
                        );
                    }
                    return existing;
                });
    }

    /**
     * Marca la key como PROCESSING antes de ejecutar la lógica.
     */
    //todo: usar mapper y devovel un dto
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public IdempotencyKeyEntity markAsProcessing(String key, String endpoint, String requestHash) {
        IdempotencyKeyEntity entity = new IdempotencyKeyEntity();
        entity.setKey(key);
        entity.setEndpoint(endpoint);
        entity.setRequestHash(requestHash);
        entity.setStatus(IdempotencyStatus.PROCESSING);
        return idempotencyKeyRepository.save(entity);
    }

    /**
     * Actualiza la key con la respuesta final (SUCCESS o FAILED).
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markAsCompleted(
            String key,
            IdempotencyStatus status,
            Object responseBody,
            Integer statusCode
    ) {
        idempotencyKeyRepository.findById(key).ifPresent(entity -> {
            try {
                entity.setStatus(status);
                entity.setStatusCode(statusCode);
                entity.setResponseBody(objectMapper.writeValueAsString(responseBody));
                idempotencyKeyRepository.save(entity);
            } catch (JsonProcessingException e) {
                log.error("Error serializing response for idempotency key {}", key, e);
            }
        });
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void delete(String key) {
        idempotencyKeyRepository.deleteById(key);
    }

    /**
     * Deserializa el responseBody cacheado al tipo deseado.
     */
    public <T> T deserializeResponse(IdempotencyKeyEntity entity, Class<T> type) {
        try {
            return objectMapper.readValue(entity.getResponseBody(), type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error deserializing cached response", e);
        }
    }
}
