package org.homeservice.service;

import org.homeservice.entity.Customer;
import org.homeservice.entity.Order;
import org.homeservice.entity.Specialist;
import org.homeservice.service.base.BaseService;

import java.time.LocalDateTime;
import java.util.List;

public interface CustomerService extends BaseService<Customer,Long> {
    Order saveOrder(String description, Double offerPrice, LocalDateTime workingTime, String address);

    List<Specialist> findAllVerifiedSpecialist();

    void saveRate(Long customerId, Double score, String comment, Long orderId);
}
