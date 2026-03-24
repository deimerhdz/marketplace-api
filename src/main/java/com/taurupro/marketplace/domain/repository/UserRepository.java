package com.taurupro.marketplace.domain.repository;

import com.taurupro.marketplace.domain.dto.UserDto;

import java.util.Optional;

public interface UserRepository {
    UserDto save(UserDto user);
    Optional<UserDto> findByEmail(String email);
    Optional<UserDto> findByCognitoSub(String cognitoSub);
    boolean existsByEmail(String email);
}
