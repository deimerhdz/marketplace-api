package com.taurupro.marketplace.persistence.repository;


import com.taurupro.marketplace.domain.dto.SupplierDto;
import com.taurupro.marketplace.domain.repository.SupplierRepository;
import com.taurupro.marketplace.persistence.crud.CrudSupplierEntity;
import com.taurupro.marketplace.persistence.entity.SupplierEntity;
import com.taurupro.marketplace.persistence.mapper.SupplierMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class SupplierEntityRepository implements SupplierRepository {
    private final CrudSupplierEntity crudSupplierEntity;
    private final SupplierMapper supplierMapper;

    public SupplierEntityRepository(CrudSupplierEntity crudSupplierEntity, SupplierMapper supplierMapper) {
        this.crudSupplierEntity = crudSupplierEntity;
        this.supplierMapper = supplierMapper;
    }

    @Override
    public SupplierDto save(SupplierDto supplier) {
        SupplierEntity supplierEntity= this.supplierMapper.toEntity(supplier);

        return this.supplierMapper.toDto(this.crudSupplierEntity.save(supplierEntity));
    }

    @Override
    public Optional<SupplierDto> findByUserId(UUID userId) {
        return this.crudSupplierEntity.findByUserId(userId).map(this.supplierMapper::toDto);
    }

    @Override
    public boolean existsByNit(String nit) {
        return this.crudSupplierEntity.existsByNit(nit);
    }

    @Override
    public boolean existsByEmail(String email) {
        return this.crudSupplierEntity.existsByEmail(email);
    }
}
