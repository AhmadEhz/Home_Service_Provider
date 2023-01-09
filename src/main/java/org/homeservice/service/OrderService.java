package org.homeservice.service;

import org.homeservice.entity.Order;
import org.homeservice.service.base.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public interface OrderService extends BaseService<Order, Long> {

    void save(Order order, Long customerId, Long subServiceId);

    List<Order> loadAllByCustomer(Long customerId);

    List<Order> loadAllBySpecialist(Long specialistId);

    List<Order> loadAllBySpecialistSubServices(Long specialistId);

    List<Order> loadAllByWaitingStatusAndSpecialist(Long specialistId);

    List<Order> loadAllByFilter(Map<String, String> filters);

    List<Order> loadAllWithDetails(Map<String, String> filters);

    void selectBid(Long bidId, Long customerId);

    void changeStatusToChooseSpecialist(Long id, Long customerId);

    void changeStatusToStarted(Long id, Long customerId);

    void changeStatusToEnded(Long id, Long customerId);
    
    void setRateId(Long orderId, Long rateId);

    boolean isAcceptedBid(Long id);
}
