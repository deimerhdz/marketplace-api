package com.taurupro.marketplace.domain.service;

import com.taurupro.marketplace.domain.dto.CreateStrawDto;
import com.taurupro.marketplace.domain.dto.StrawDto;
import com.taurupro.marketplace.domain.dto.UpdateStrawDto;
import com.taurupro.marketplace.domain.repository.StrawRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class StrawService {
    private final StrawRepository strawRepository;

    public StrawService(StrawRepository strawRepository) {
        this.strawRepository = strawRepository;
    }

    public List<StrawDto> list(UUID bullId) {
      return  this.strawRepository.list(bullId);
    }
    public void save(CreateStrawDto createStrawDto) {
        this.strawRepository.save(createStrawDto);
    }

    public void update(UUID id, UpdateStrawDto updateStrawDto) {
        this.strawRepository.update(id, updateStrawDto);
    }

}
