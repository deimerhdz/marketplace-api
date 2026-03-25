package com.taurupro.marketplace.persistence.crud;

import com.taurupro.marketplace.persistence.entity.PaymentEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.UUID;

public interface CrudPaymentEntity  extends ListCrudRepository<PaymentEntity, UUID> {
}
