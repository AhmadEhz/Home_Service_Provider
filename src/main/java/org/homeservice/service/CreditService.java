package org.homeservice.service;

import org.homeservice.entity.Credit;
import org.homeservice.service.base.BaseService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface CreditService extends BaseService<Credit, Long> {
    void deposit(Long id, Long amount);

    void withdraw(Long id, Long withdrawalAmount);

    Optional<Credit> loadBySpecialist(Long specialistId);

    Optional<Credit> loadByCustomer(Long customerId);

    void cardToCard(Long sourceId, Long destinationId, Long amount);
}
