package com.taurupro.marketplace.domain.service;

import com.taurupro.marketplace.domain.dto.CatalogDetailDto;
import org.springframework.stereotype.Service;
import com.taurupro.marketplace.domain.dto.CatalogDto;
import com.taurupro.marketplace.domain.repository.BullRepository;

import java.util.List;

@Service
public class CatalogService {

    private final BullRepository bullRepository;

    public CatalogService(BullRepository bullRepository) {
        this.bullRepository = bullRepository;
    }

    public List<CatalogDto> getAll(String name){
        return this.bullRepository.getByBreedName(name);
    }
    public CatalogDetailDto getDetailBySlug(String slug){
        return this.bullRepository.getBySlug(slug);
    }

    public List<CatalogDto> getRecentBulls(Integer limit) {
        return this.bullRepository.getRecentBulls(limit);
    }
}
