package org.homeservice.service;

import org.homeservice.entity.Order;
import org.homeservice.service.base.BaseService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public interface OrderService extends BaseService<Order, Long> {

    List<Order> findAllByCustomer(Long customerId);

    List<Order> findAllBySpecialist(Long specialistId);

    void selectBidForOffer(Long bidId, Long customerId);

    void changeStatusToChooseSpecialist(Long id, Long customerId);

    void changeStatusToStarted(Long id, Long customerId);

    boolean isAcceptedBid(Long id);
}
