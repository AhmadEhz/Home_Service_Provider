package org.homeservice.service;

import org.homeservice.entity.Rate;
import org.homeservice.service.base.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public interface RateService extends BaseService<Rate, Long> {
    void save(Rate rate, Long orderId, Long customerId);
}
