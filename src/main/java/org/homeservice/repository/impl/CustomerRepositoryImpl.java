package org.homeservice.repository.impl;

import org.homeservice.entity.Customer;
import org.homeservice.repository.CustomerRepository;
import org.homeservice.repository.base.BaseRepositoryImpl;

public class CustomerRepositoryImpl extends BaseRepositoryImpl<Customer, Long>
        implements CustomerRepository {
    private static CustomerRepository repository;

    private CustomerRepositoryImpl() {
    }

    @Override
    protected Class<Customer> getEntityClass() {
        return Customer.class;
    }

    public static CustomerRepository getRepository() {
        if (repository == null)
            repository = new CustomerRepositoryImpl();
        return repository;
    }
}
