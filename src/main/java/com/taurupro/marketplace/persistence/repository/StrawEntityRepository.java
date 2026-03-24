package com.taurupro.marketplace.persistence.repository;

import com.taurupro.marketplace.domain.dto.CreateStrawDto;
import com.taurupro.marketplace.domain.dto.StrawDto;
import com.taurupro.marketplace.domain.dto.UpdateStrawDto;
import com.taurupro.marketplace.domain.repository.StrawRepository;
import com.taurupro.marketplace.persistence.crud.CrudStrawEntity;
import com.taurupro.marketplace.persistence.entity.StrawEntity;
import com.taurupro.marketplace.persistence.mapper.StrawMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class StrawEntityRepository implements StrawRepository {
    private final CrudStrawEntity crudStrawEntity;
    private final StrawMapper strawMapper;

    private static final String PREFIX = "SKU";

    public StrawEntityRepository(CrudStrawEntity crudStrawEntity, StrawMapper strawMapper) {
        this.crudStrawEntity = crudStrawEntity;
        this.strawMapper = strawMapper;
    }

    @Override
    public void save(CreateStrawDto createStrawDto) {
        StrawEntity strawEntity = this.strawMapper.toEntity(createStrawDto);
        strawEntity.setSku(this.generateSku());
        this.crudStrawEntity.save(strawEntity);
    }

    @Override
    public void update(UUID id, UpdateStrawDto updateStrawDto) {
        StrawEntity strawEntity = this.crudStrawEntity.findById(id).orElse(null);
        if (strawEntity == null) throw new RuntimeException("Pajilla no encontrada");

        this.strawMapper.updateEntityFromDto(updateStrawDto, strawEntity);
        this.crudStrawEntity.save(strawEntity);
    }

    @Override
    public List<StrawDto> list(UUID bullId) {
        return this.strawMapper.toDto(crudStrawEntity.findByBullId(bullId));
    }

    public String generateSku() {
        String timestamp = String.valueOf(System.currentTimeMillis()).substring(5); // 8 dígitos
        String random = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        return PREFIX + "-" + timestamp + "-" + random;
    }
}
