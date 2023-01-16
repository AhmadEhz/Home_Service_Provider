package org.homeservice.service;

import org.homeservice.entity.Customer;
import org.homeservice.entity.Order;
import org.homeservice.service.base.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface OrderService extends BaseService<Order, Long> {

    void save(Order order, Customer customer, Long subServiceId);

    List<Order> loadAllByCustomer(Long customerId);

    List<Order> loadAllBySpecialist(Long specialistId);

    List<Order> loadAllBySpecialistSubServices(Long specialistId);

    List<Order> loadAllByWaitingStatusAndSpecialist(Long specialistId);

    List<Order> loadAllByFilterAndCustomer(Map<String, String> filters, Long customerId);

    List<Order> loadAllByFilterAndSpecialist(Map<String, String> filters, Long specialistId);

    List<Order> loadAllWithDetails(Map<String, String> filters);

    void selectBid(Long bidId, Customer customer);

    void changeStatusToChooseSpecialist(Order order, Customer customer);

    void changeStatusToStarted(Long id, Customer customer);

    void changeStatusToEnded(Long id, Customer customer);
    
    void setRateId(Long orderId, Long rateId);

    boolean isAcceptedBid(Long id);
}
