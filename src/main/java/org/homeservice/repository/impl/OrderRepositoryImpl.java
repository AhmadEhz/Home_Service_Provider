package org.homeservice.repository.impl;

import org.homeservice.entity.Order;
import org.homeservice.repository.OrderRepository;
import org.homeservice.repository.base.BaseRepositoryImpl;

public class OrderRepositoryImpl extends BaseRepositoryImpl<Order, Long> implements OrderRepository {
    private static OrderRepository repository;

    private OrderRepositoryImpl() {
    }

    @Override
    protected Class<Order> getEntityClass() {
        return Order.class;
    }

    public static OrderRepository getRepository() {
        if (repository == null)
            repository = new OrderRepositoryImpl();
        return repository;
    }
}
