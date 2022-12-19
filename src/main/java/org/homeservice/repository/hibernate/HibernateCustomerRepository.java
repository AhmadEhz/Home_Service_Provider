package org.homeservice.repository.hibernate;

import org.homeservice.entity.Customer;
import org.homeservice.repository.hibernate.base.HibernateBaseRepository;

import java.util.Optional;

public interface HibernateCustomerRepository extends HibernateBaseRepository<Customer,Long> {
    Optional<Customer> findByUsername(String username);

    Optional<Customer> findByEmail(String email);
}
