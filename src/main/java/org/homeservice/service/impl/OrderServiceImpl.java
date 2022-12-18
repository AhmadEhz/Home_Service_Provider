package org.homeservice.service.impl;

import org.homeservice.entity.Order;
import org.homeservice.repository.OrderRepository;
import org.homeservice.service.OrderService;
import org.homeservice.service.base.BaseServiceImpl;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Scope("singleton")
public class OrderServiceImpl extends BaseServiceImpl<Order,Long,OrderRepository> implements OrderService {

    public OrderServiceImpl(OrderRepository repository) {
        super(repository);
    }

    @Override
    public List<Order> findAllByCustomer(Long customerId) {
        return repository.findAllByCustomer_Id(customerId);
    }

    @Override
    public List<Order> findAllBySpecialist(Long specialistId) {
        return repository.findAllBySpecialist_Id(specialistId);
    }

}
