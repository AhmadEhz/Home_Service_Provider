package org.homeservice.repository.hibernate.impl;

import org.homeservice.entity.Order;
import org.homeservice.repository.hibernate.HibernateOrderRepository;
import org.homeservice.repository.hibernate.base.HibernateBaseRepositoryImpl;

public class HibernateOrderRepositoryImpl extends HibernateBaseRepositoryImpl<Order, Long> implements HibernateOrderRepository {
    private static HibernateOrderRepository repository;

    private HibernateOrderRepositoryImpl() {
    }

    @Override
    protected Class<Order> getEntityClass() {
        return Order.class;
    }

    public static HibernateOrderRepository getRepository() {
        if (repository == null)
            repository = new HibernateOrderRepositoryImpl();
        return repository;
    }
}
