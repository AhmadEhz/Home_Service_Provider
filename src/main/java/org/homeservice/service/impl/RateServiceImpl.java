package org.homeservice.service.impl;

import org.homeservice.entity.Rate;
import org.homeservice.repository.RateRepository;
import org.homeservice.service.RateService;
import org.homeservice.service.base.BaseServiceImpl;

public class RateServiceImpl extends BaseServiceImpl<Rate, Long, RateRepository> implements RateService {
    protected RateServiceImpl(RateRepository repository) {
        super(repository);
    }
}
