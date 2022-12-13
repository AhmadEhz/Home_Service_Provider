package org.homeservice.service.impl;

import org.homeservice.entity.Order;
import org.homeservice.repository.OrderRepository;
import org.homeservice.service.OrderService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Scope("singleton")
public class OrderServiceImpl implements OrderService {
    OrderRepository repository;

    public OrderServiceImpl(OrderRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(Order order) {
        repository.save(order);
    }

    @Override
    public Optional<Order> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Order> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Order> findAllByCustomer(Long customerId) {
        return repository.findAllByCustomer_Id(customerId);
    }

    @Override
    public List<Order> findAllBySpecialist(Long specialistId) {
        return repository.findAllBySpecialist_Id(specialistId);
    }
    @Override
    public void delete(Order order) {
        repository.delete(order);
    }

}
