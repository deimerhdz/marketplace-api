package com.taurupro.marketplace.persistence.repository;

import com.taurupro.marketplace.domain.dto.*;
import com.taurupro.marketplace.domain.enums.Resource;
import com.taurupro.marketplace.domain.repository.BullRepository;
import com.taurupro.marketplace.persistence.crud.BullPagSortRepository;
import com.taurupro.marketplace.persistence.crud.CrudBullEntity;
import com.taurupro.marketplace.persistence.entity.BullEntity;
import com.taurupro.marketplace.persistence.mapper.BullMapper;
import com.taurupro.marketplace.persistence.model.MediaFile;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class BullEntityRepository implements BullRepository {
    private final CrudBullEntity crudBullEntity;
    private final BullMapper bullMapper;
    private static final int MAX_GALLERY_SIZE = 3;
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
    public ResourceUpdateResult  updateResource(UUID id, UUID supplierId,  List<MediaFile> files, Resource resource) {
        BullEntity bullEntity = this.crudBullEntity.findByIdAndSupplierId(id, supplierId)
                .orElseThrow(() -> new EntityNotFoundException("Bull not found: " + id));

        // Extrae old keys ANTES de pisar
        List<String> oldKeys = switch (resource) {
            case IMAGE    -> bullEntity.getImage()   != null ? List.of(bullEntity.getImage().getKey())    : List.of();
            case VIDEO    -> bullEntity.getVideo()   != null ? List.of(bullEntity.getVideo().getKey())    : List.of();
            case DOCUMENT -> bullEntity.getGeneticEvaluation() != null ? List.of(bullEntity.getGeneticEvaluation().getKey()) : List.of();
            case GALLERY  -> bullEntity.getGallery() != null
                    ? bullEntity.getGallery().stream().map(MediaFile::getKey).toList()
                    : List.of();
        };

        // Actualiza
        switch (resource) {
            case IMAGE    -> bullEntity.setImage(files.isEmpty() ? null : files.getFirst());
            case VIDEO    -> bullEntity.setVideo(files.isEmpty() ? null : files.getFirst());
            case DOCUMENT -> bullEntity.setGeneticEvaluation(files.isEmpty() ? null : files.getFirst());
            case GALLERY  -> {
                List<MediaFile> currentGallery = new ArrayList<>(bullEntity.getGallery());
                int available = MAX_GALLERY_SIZE - currentGallery.size();

                if (available <= 0) {
                    throw new IllegalStateException("Gallery is full, max " + MAX_GALLERY_SIZE + " images");
                }

                currentGallery.addAll(files.stream().limit(available).toList());
                bullEntity.setGallery(currentGallery);
            }
        }

        BullDto updated = this.bullMapper.toDto(this.crudBullEntity.save(bullEntity));
        return new ResourceUpdateResult(updated, oldKeys);
    }
    @Override
    public ResourceUpdateResult replaceGallery(UUID id, UUID supplierId, List<MediaFile> files) {
        BullEntity bullEntity = this.crudBullEntity.findByIdAndSupplierId(id, supplierId)
                .orElseThrow(() -> new EntityNotFoundException("Bull not found: " + id));

        List<String> oldKeys = bullEntity.getGallery().stream()
                .map(MediaFile::getKey)
                .toList();

        bullEntity.setGallery(new ArrayList<>(files)); // ✅ reemplaza directamente

        BullDto updated = this.bullMapper.toDto(this.crudBullEntity.save(bullEntity));
        return new ResourceUpdateResult(updated, oldKeys);
    }
    @Override
    public List<CatalogDto> getRecentBulls(int limit) {
        LocalDateTime now = LocalDateTime.now();
        Pageable pageRequest = (Pageable) PageRequest.of(0, limit);
        // 1. Intento: últimos 7 días
        List<BullEntity> bulls = crudBullEntity.findByCreatedAtAfterOrderByCreatedAtDesc(
                now.minusDays(7),
                pageRequest
        );

        if (!bulls.isEmpty()) return this.bullMapper.toDto(bulls);

        // 2. Intento: últimos 30 días
        bulls = crudBullEntity.findByCreatedAtAfterOrderByCreatedAtDesc(
                now.minusDays(30),
                PageRequest.of(0, limit)
        );

        if (!bulls.isEmpty()) return this.bullMapper.toDto(bulls);;

        // 3. Fallback: TODOS ordenados por fecha
        return this.bullMapper.toDto(crudBullEntity.findAllByOrderByCreatedAtDesc(
                PageRequest.of(0, limit))
        );
    }

    private String generateSlug(String text) {
        return text
                .toLowerCase()
                .trim()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-");
    }
}
