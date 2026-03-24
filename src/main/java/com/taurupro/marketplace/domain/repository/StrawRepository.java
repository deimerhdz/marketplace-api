package com.taurupro.marketplace.domain.repository;

import com.taurupro.marketplace.domain.dto.CreateStrawDto;
import com.taurupro.marketplace.domain.dto.StrawDto;
import com.taurupro.marketplace.domain.dto.UpdateStrawDto;

import java.util.List;
import java.util.UUID;

public interface StrawRepository {
    void save(CreateStrawDto createStrawDto);
    void update(UUID id,UpdateStrawDto updateStrawDto);
    List<StrawDto> list(UUID bullId);
}
