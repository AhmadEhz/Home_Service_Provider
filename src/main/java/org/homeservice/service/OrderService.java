package org.homeservice.service;

import org.homeservice.entity.Order;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface OrderService {

    void save(Order order);

    Optional<Order> findById(Long id);

    List<Order> findAll();

    List<Order> findAllByCustomer(Long customerId);

    List<Order> findAllBySpecialist(Long specialistId);

    void delete(Order order);
}
