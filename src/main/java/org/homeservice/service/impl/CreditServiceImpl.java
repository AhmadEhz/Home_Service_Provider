package org.homeservice.service.impl;

import org.homeservice.entity.Credit;
import org.homeservice.repository.CreditRepository;
import org.homeservice.service.CreditService;
import org.homeservice.service.base.BaseServiceImpl;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
public class CreditServiceImpl extends BaseServiceImpl<Credit, Long, CreditRepository> implements CreditService {
    protected CreditServiceImpl(CreditRepository repository) {
        super(repository);
    }
}
