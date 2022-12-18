package org.homeservice.service;

import org.homeservice.entity.Bid;
import org.homeservice.service.base.BaseService;
import org.springframework.stereotype.Service;

@Service
public interface BidService extends BaseService<Bid, Long> {
}
