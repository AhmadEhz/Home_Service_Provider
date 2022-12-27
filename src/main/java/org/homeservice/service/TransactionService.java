package org.homeservice.service;

import org.homeservice.entity.Transaction;
import org.homeservice.service.base.BaseService;
import org.springframework.stereotype.Service;

@Service
public interface TransactionService extends BaseService<Transaction, Long> {
}
