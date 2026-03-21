package com.taurupro.marketplace.domain.repository;

import com.taurupro.marketplace.domain.dto.BreedDto;

import java.util.List;

public interface BreedRepository {
    List<BreedDto> getAll();


}
