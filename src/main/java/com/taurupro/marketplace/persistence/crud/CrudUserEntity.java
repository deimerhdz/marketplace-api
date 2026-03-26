package com.taurupro.marketplace.persistence.crud;
import com.taurupro.marketplace.persistence.entity.UserEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface CrudUserEntity  extends ListCrudRepository<UserEntity, UUID> {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByCognitoSub(String cognitoSub);
    boolean existsByEmail(String email);
}
