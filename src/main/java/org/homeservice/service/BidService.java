package org.homeservice.service;

import org.homeservice.entity.Bid;
import org.homeservice.entity.Specialist;
import org.homeservice.service.base.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface BidService extends BaseService<Bid, Long> {
    void save(Bid bid, Long orderId, Specialist specialist);

    List<Bid> loadAllByOrder(Long orderId, String sortBy);

    Optional<Bid> loadByCustomerAndSpecialist(Long customerId, Long specialistId);

    Optional<Bid> loadByOrderId(Long orderId);
}
