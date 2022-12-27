package org.homeservice.service.impl;

import org.homeservice.entity.Transaction;
import org.homeservice.repository.TransactionRepository;
import org.homeservice.service.TransactionService;
import org.homeservice.service.base.BaseServiceImpl;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
public class TransactionServiceImpl extends BaseServiceImpl<Transaction, Long, TransactionRepository>
        implements TransactionService {
    protected TransactionServiceImpl(TransactionRepository repository) {
        super(repository);
    }
}
