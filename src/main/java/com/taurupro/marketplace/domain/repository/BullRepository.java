package com.taurupro.marketplace.domain.repository;

import com.taurupro.marketplace.domain.dto.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;


public interface BullRepository {

    List<CatalogDto> getByBreedName(String name);

    CatalogDetailDto getBySlug(String slug);

    Page<BullDto> getAll(int page, int elements, UUID supplierId);

    BullDto getById(UUID id, UUID supplierId);

    BullDto save(UUID supplierId, CreateBullDto createBullDto);

    BullDto update(UUID id, UUID supplierId, UpdateBullDto bullDto);
}
