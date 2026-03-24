package com.taurupro.marketplace.persistence.crud;

import com.taurupro.marketplace.persistence.entity.BullEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.ListPagingAndSortingRepository;

import java.util.UUID;

public interface BullPagSortRepository extends ListPagingAndSortingRepository <BullEntity, UUID>{

    Page<BullEntity> findBySupplierId(UUID supplierId, Pageable pageable);
}
