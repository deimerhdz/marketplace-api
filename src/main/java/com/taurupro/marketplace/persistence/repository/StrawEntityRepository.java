package com.taurupro.marketplace.persistence.repository;

import com.taurupro.marketplace.domain.dto.CreateStrawDto;
import com.taurupro.marketplace.domain.dto.StrawDto;
import com.taurupro.marketplace.domain.dto.UpdateStrawDto;
import com.taurupro.marketplace.domain.repository.StrawRepository;
import com.taurupro.marketplace.persistence.crud.CrudInventoryEntity;
import com.taurupro.marketplace.persistence.crud.CrudStrawEntity;
import com.taurupro.marketplace.persistence.entity.BullEntity;
import com.taurupro.marketplace.persistence.entity.InventoryEntity;
import com.taurupro.marketplace.persistence.entity.StrawEntity;
import com.taurupro.marketplace.persistence.mapper.InventoryMapper;
import com.taurupro.marketplace.persistence.mapper.StrawMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class StrawEntityRepository implements StrawRepository {
    private final CrudStrawEntity crudStrawEntity;
    private final CrudInventoryEntity crudInventoryEntity;
    private final StrawMapper strawMapper;
    private final InventoryMapper inventoryMapper;

    private static final String PREFIX = "SKU";

    public StrawEntityRepository(CrudStrawEntity crudStrawEntity,
                                 CrudInventoryEntity crudInventoryEntity,
                                 StrawMapper strawMapper,
                                 InventoryMapper inventoryMapper) {
        this.strawMapper = strawMapper;
        this.inventoryMapper= inventoryMapper;
        this.crudStrawEntity = crudStrawEntity;
        this.crudInventoryEntity=crudInventoryEntity;
    }

    @Override
    public Optional<StrawDto> findById(UUID id) {

        return this.crudStrawEntity.findById(id).map(this.strawMapper::toDto);
    }

    @Transactional
    @Override
    public void save(CreateStrawDto createStrawDto) {
        StrawEntity strawEntity = this.strawMapper.toEntity(createStrawDto);
        strawEntity.setSku(this.generateSku());
        this.crudStrawEntity.save(strawEntity);
        System.out.println(createStrawDto.inventory());
        InventoryEntity inventoryEntity = this.inventoryMapper.toEntity(createStrawDto.inventory());
        inventoryEntity.setIsAvailable(true);
        inventoryEntity.setStrawId(strawEntity.getId());
        this.crudInventoryEntity.save(inventoryEntity);

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
