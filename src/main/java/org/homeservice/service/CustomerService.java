package org.homeservice.service;

import org.homeservice.entity.Customer;
import org.homeservice.entity.Order;
import org.homeservice.entity.Rate;
import org.homeservice.entity.Specialist;
import org.homeservice.service.base.BaseService;

import java.time.LocalDateTime;
import java.util.List;

public interface CustomerService extends BaseService<Customer,Long> {
    Customer save(String firstName, String lastName, String username, String email, String password);

    Order saveOrder(String description, Double offerPrice, LocalDateTime workingTime, String address);

    List<Specialist> findAllVerifiedSpecialist();

    Rate saveRate(Long customerId, Double score, String comment, Long orderId);

    Rate saveRate(Long customerId, Double score, Long orderId);

    boolean isExistedUsername(String username);

    boolean isExistedEmail(String email);
}
