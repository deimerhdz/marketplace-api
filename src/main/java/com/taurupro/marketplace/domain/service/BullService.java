package com.taurupro.marketplace.domain.service;

import com.taurupro.marketplace.domain.dto.*;
import com.taurupro.marketplace.domain.enums.Resource;
import com.taurupro.marketplace.domain.repository.BullRepository;
import com.taurupro.marketplace.persistence.model.MediaFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class BullService {
    private final BullRepository bullRepository;

    private final FileService fileService;

    public BullService(BullRepository bullRepository,FileService fileService) {
        this.fileService = fileService;
        this.bullRepository = bullRepository;
    }

    public BullDto save(UUID supplierId, CreateBullDto createBullDto) {
        return this.bullRepository.save(supplierId, createBullDto);
    }

    public BullDto update(UUID id, UUID supplierId, UpdateBullDto updateBullDto) {
        return this.bullRepository.update(id, supplierId, updateBullDto);
    }

    public BullDto getById(UUID id, UUID supplierId) {
        return this.bullRepository.getById(id, supplierId);
    }

    public Page<BullDto> getAll(int page, int elements, UUID supplierId) {
        return this.bullRepository.getAll(page, elements, supplierId);
    }

    public BullDto updateResource(UUID bullId, UUID supplierId, List<MediaFileDto> files, Resource resource) {
        List<MediaFile> mediaFiles = files.stream().map(f-> new MediaFile(f.key(),f.contentType())).toList();
        ResourceUpdateResult result = bullRepository.updateResource(bullId, supplierId, mediaFiles, resource);
        deleteFilesSafely(result.oldKeys());
        return result.updated();
    }


    public BullDto deleteResource(UUID bullId, UUID supplierId, Resource resource, String key) {
        System.out.println(resource);
        System.out.println(key);
        ResourceUpdateResult result = switch (resource) {
            case GALLERY ->  {
                BullDto current = bullRepository.getById(bullId, supplierId);
                List<MediaFile> updatedGallery = current.gallery().stream()
                        .filter(img -> !img.key().equals(key))
                        .map(f -> new MediaFile(f.key(), f.contentType()))
                        .toList();
                yield bullRepository.replaceGallery(bullId, supplierId, updatedGallery); // ✅
            }
            case IMAGE, VIDEO, DOCUMENT ->
                    bullRepository.updateResource(bullId, supplierId, List.of(), resource);
        };
        System.out.println("result"+result);
        deleteFilesSafely(List.of(key));
        return result.updated();
    }

    private void deleteFilesSafely(List<String> keys) {
        keys.stream()
                .filter(key -> key != null && !key.isBlank())
                .forEach(key -> {
                    try {
                        fileService.deleteFile(key);
                    } catch (Exception e) {
                        log.warn("Imagen huérfana en S3, key: {}", key, e);
                    }
                });
    }


}
