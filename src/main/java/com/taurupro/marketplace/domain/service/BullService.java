package com.taurupro.marketplace.domain.service;

import com.taurupro.marketplace.domain.dto.BullDto;
import com.taurupro.marketplace.domain.dto.CreateBullDto;
import com.taurupro.marketplace.domain.dto.UpdateBullDto;
import com.taurupro.marketplace.domain.repository.BullRepository;
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
        return this.bullRepository.update(id,supplierId, updateBullDto);
    }

    public BullDto getById(UUID id,UUID supplierId){
        return this.bullRepository.getById(id,supplierId);
    }

    public Page<BullDto> getAll(int page, int elements,UUID supplierId) {
        return this.bullRepository.getAll(page, elements,supplierId);
    }
}
