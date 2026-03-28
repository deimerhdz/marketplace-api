package com.taurupro.marketplace.domain.repository;

import com.taurupro.marketplace.domain.dto.CreateStrawDto;
import com.taurupro.marketplace.domain.dto.StrawDto;
import com.taurupro.marketplace.domain.dto.UpdateStrawDto;
import com.taurupro.marketplace.domain.enums.StrawType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StrawRepository {

    Optional<StrawDto> findById(UUID id);
    Optional<StrawDto> findByType(StrawType type,UUID bullId);
    void save(CreateStrawDto createStrawDto);
    void update(UUID id,UpdateStrawDto updateStrawDto);
    List<StrawDto> list(UUID bullId);
}
