package com.taurupro.marketplace.persistence.repository;

import com.taurupro.marketplace.domain.dto.UserDto;
import com.taurupro.marketplace.domain.repository.UserRepository;
import com.taurupro.marketplace.persistence.crud.CrudUserEntity;
import com.taurupro.marketplace.persistence.crud.UserPagSortRepository;
import com.taurupro.marketplace.persistence.entity.UserEntity;
import com.taurupro.marketplace.persistence.mapper.UserMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserEntityRepository implements UserRepository {
    private final CrudUserEntity crudUserEntity;
    private final UserPagSortRepository userPagSortRepository;

    private final UserMapper userMapper;

    public UserEntityRepository(CrudUserEntity crudUserEntity, UserMapper userMapper,UserPagSortRepository userPagSortRepository) {
        this.crudUserEntity = crudUserEntity;
        this.userMapper = userMapper;
        this.userPagSortRepository=userPagSortRepository;
    }

    @Override
    public Page<UserDto> getAll(int page, int elements) {
        Pageable pageRequest = (Pageable) PageRequest.of(page, elements);
        return this.userPagSortRepository.findAll(pageRequest).map(this.userMapper::toDto);
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
