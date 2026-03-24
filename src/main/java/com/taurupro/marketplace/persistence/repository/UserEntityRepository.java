package com.taurupro.marketplace.persistence.repository;

import com.taurupro.marketplace.domain.dto.UserDto;
import com.taurupro.marketplace.domain.repository.UserRepository;
import com.taurupro.marketplace.persistence.crud.CrudUserEntity;
import com.taurupro.marketplace.persistence.entity.UserEntity;
import com.taurupro.marketplace.persistence.mapper.UserMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserEntityRepository implements UserRepository {
    private final CrudUserEntity crudUserEntity;

    private final UserMapper userMapper;

    public UserEntityRepository(CrudUserEntity crudUserEntity, UserMapper userMapper) {
        this.crudUserEntity = crudUserEntity;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto save(UserDto user) {
        //add validations
        UserEntity userEntity = this.userMapper.toEntity(user);

        return this.userMapper.toDto(this.crudUserEntity.save(userEntity));
    }

    @Override
    public Optional<UserDto> findByEmail(String email) {
        return this.crudUserEntity.findByEmail(email).map(this.userMapper::toDto);
    }

    @Override
    public Optional<UserDto> findByCognitoSub(String cognitoSub) {
        return this.crudUserEntity.findByCognitoSub(cognitoSub).map(this.userMapper::toDto);
    }


    @Override
    public boolean existsByEmail(String email) {
        return this.crudUserEntity.existsByEmail(email);
    }
}
