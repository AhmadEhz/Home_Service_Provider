package org.homeservice.service;

import org.homeservice.entity.Order;
import org.homeservice.service.base.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService extends BaseService<Order, Long> {

    void save(Order order, Long customerId, Long subServiceId);

    List<Order> findAllByCustomer(Long customerId);

    List<Order> findAllBySpecialist(Long specialistId);

    List<Order> loadAllBySpecialistSubServices(Long specialistId);

    List<Order> loadAllByWaitingStatusAndSpecialist(Long specialistId);

    void selectBid(Long bidId, Long customerId);

    void changeStatusToChooseSpecialist(Long id, Long customerId);

    void changeStatusToStarted(Long id, Long customerId);

    boolean isAcceptedBid(Long id);
}
