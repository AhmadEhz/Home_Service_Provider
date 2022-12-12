package org.homeservice.repository.hibernate;

import org.homeservice.entity.Customer;
import org.homeservice.repository.hibernate.base.BaseRepository;

import java.util.Optional;

public interface CustomerRepository extends BaseRepository<Customer,Long> {
    Optional<Customer> findByUsername(String username);

    Optional<Customer> findByEmail(String email);
}
