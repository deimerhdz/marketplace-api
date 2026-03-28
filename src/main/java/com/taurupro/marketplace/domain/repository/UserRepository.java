package com.taurupro.marketplace.domain.repository;

import com.taurupro.marketplace.domain.dto.UserDto;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface UserRepository {

    Page<UserDto> getAll(int page, int elements);
    UserDto save(UserDto user);
    Optional<UserDto> findByEmail(String email);
    Optional<UserDto> findByCognitoSub(String cognitoSub);
    boolean existsByEmail(String email);
}
