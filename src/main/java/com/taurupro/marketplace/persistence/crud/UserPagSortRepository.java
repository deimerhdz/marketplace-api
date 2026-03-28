package com.taurupro.marketplace.persistence.crud;

import com.taurupro.marketplace.persistence.entity.UserEntity;
import org.springframework.data.repository.ListPagingAndSortingRepository;

import java.util.UUID;

public interface UserPagSortRepository extends ListPagingAndSortingRepository <UserEntity, UUID>{
}
