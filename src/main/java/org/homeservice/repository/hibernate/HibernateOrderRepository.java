package org.homeservice.repository.hibernate;

import org.homeservice.entity.Order;
import org.homeservice.repository.hibernate.base.HibernateBaseRepository;

public interface HibernateOrderRepository extends HibernateBaseRepository<Order,Long> {
}
