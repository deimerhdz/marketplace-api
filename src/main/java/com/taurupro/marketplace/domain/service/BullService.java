package com.taurupro.marketplace.domain.service;

import com.taurupro.marketplace.domain.dto.*;
import com.taurupro.marketplace.domain.repository.BullRepository;
import com.taurupro.marketplace.persistence.model.MediaFile;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BullService {
    private final BullRepository bullRepository;

    public BullService(BullRepository bullRepository) {
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

    public BullDto updateImage(UUID bullId, UUID supplierId, UpdateBullImageDto dto) {


        MediaFile image = new MediaFile(dto.key(), dto.contentType());

        return bullRepository.updateImage(bullId, supplierId, image);
    }
}
