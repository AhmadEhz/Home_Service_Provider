package org.homeservice.service.impl;

import org.homeservice.entity.Rate;
import org.homeservice.repository.RateRepository;
import org.homeservice.service.RateService;
import org.homeservice.service.SpecialistService;
import org.homeservice.service.base.BaseServiceImpl;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
public class RateServiceImpl extends BaseServiceImpl<Rate, Long, RateRepository> implements RateService {

    SpecialistService specialistService;

    public RateServiceImpl(RateRepository repository, SpecialistService specialistService) {
        super(repository);
        this.specialistService = specialistService;
    }

    @Override
    public void save(Rate rate) {
        super.save(rate);
        specialistService.updateScoreByRateId(rate.getId());
    }
}
