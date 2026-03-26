package com.taurupro.marketplace.persistence.repository;

import com.taurupro.marketplace.domain.dto.*;
import com.taurupro.marketplace.domain.repository.BullRepository;
import com.taurupro.marketplace.persistence.crud.BullPagSortRepository;
import com.taurupro.marketplace.persistence.crud.CrudBullEntity;
import com.taurupro.marketplace.persistence.entity.BullEntity;
import com.taurupro.marketplace.persistence.mapper.BullMapper;
import com.taurupro.marketplace.persistence.model.MediaFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.UUID;

@Repository
public class BullEntityRepository implements BullRepository {
    private final CrudBullEntity crudBullEntity;
    private final BullMapper bullMapper;

    private final BullPagSortRepository bullPagSortRepository;

    public BullEntityRepository(CrudBullEntity crudBullEntity, BullMapper bullMapper, BullPagSortRepository bullPagSortRepository) {
        this.crudBullEntity = crudBullEntity;
        this.bullMapper = bullMapper;
        this.bullPagSortRepository = bullPagSortRepository;
    }

    @Override
    public List<CatalogDto> getByBreedName(String  name) {
        return this.bullMapper.toDto(this.crudBullEntity.findByBreed_Name(name));
    }

    @Override
    public CatalogDetailDto getBySlug(String slug) {
        BullEntity bullEntity =this.crudBullEntity.findBySlug(slug).orElse(null);
        return this.bullMapper.toCatalog(bullEntity);
    }

    public Page<BullDto> getAll(int page, int elements, UUID supplierId) {
        Pageable pageRequest = (Pageable) PageRequest.of(page, elements);
        return this.bullPagSortRepository.findBySupplierId(supplierId, pageRequest).map(this.bullMapper::toDto);
    }

    @Override
    public BullDto getById(UUID id, UUID supplierId) {

        BullEntity bullEntity = this.crudBullEntity.findByIdAndSupplierId(id, supplierId).orElse(null);

        return bullMapper.toDto(bullEntity);
    }

    @Override
    public BullDto save(UUID supplierId, CreateBullDto createBullDto) {
        String baseSlug = generateSlug(createBullDto.name());
        BullEntity bullEntity = this.bullMapper.toEntity(createBullDto);
        bullEntity.setSlug(baseSlug);
        bullEntity.setSupplierId(supplierId);
        return this.bullMapper.toDto(this.crudBullEntity.save(bullEntity));
    }


    @Override
    public BullDto update(UUID id, UUID supplierId, UpdateBullDto updateBullDto) {
        BullEntity bullEntity = this.crudBullEntity.findByIdAndSupplierId(id, supplierId).orElse(null);
        if (bullEntity == null) return null;

        this.bullMapper.updateEntityFromDto(updateBullDto, bullEntity);

        return this.bullMapper.toDto(this.crudBullEntity.save(bullEntity));
    }

    @Override
    public BullDto updateImage(UUID id, UUID supplierId, MediaFile image) {
        BullEntity bullEntity = this.crudBullEntity.findByIdAndSupplierId(id, supplierId).orElse(null);
        if (bullEntity == null) return null;

        bullEntity.setImage(image);

        return this.bullMapper.toDto(this.crudBullEntity.save(bullEntity));
    }

    private String generateSlug(String text) {
        return text
                .toLowerCase()
                .trim()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-");
    }
}
