package com.taurupro.marketplace.domain.repository;

import com.taurupro.marketplace.domain.dto.BullDto;
import com.taurupro.marketplace.domain.dto.CreateBullDto;
import com.taurupro.marketplace.domain.dto.UpdateBullDto;
import org.springframework.data.domain.Page;

import java.util.UUID;


public interface BullRepository {

    Page<BullDto> getAll(int page, int elements, UUID supplierId);

    BullDto getById(UUID id, UUID supplierId);

    BullDto save(UUID supplierId, CreateBullDto createBullDto);

    BullDto update(UUID id, UUID supplierId, UpdateBullDto bullDto);
}
