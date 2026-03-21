package com.taurupro.marketplace.domain.service;

import com.taurupro.marketplace.domain.dto.BreedDto;
import com.taurupro.marketplace.domain.repository.BreedRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BreedService {
    private BreedRepository breedRepository;

    public BreedService(BreedRepository breedRepository) {
        this.breedRepository = breedRepository;
    }

    public List<BreedDto> getAll(){
        return this.breedRepository.getAll();
    }

}
