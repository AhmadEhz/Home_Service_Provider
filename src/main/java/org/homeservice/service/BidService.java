package org.homeservice.service;

import org.homeservice.entity.Bid;
import org.homeservice.service.base.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BidService extends BaseService<Bid, Long> {
    void save(Bid bid, Long orderId, Long specialistId);

    List<Bid> loadAllByOrderSortedByPrice(Long orderId);

    List<Bid> loadAllByOrderSortedBySpecialistScore(Long orderId);
}
