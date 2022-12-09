package org.homeservice.repository.impl;

import org.homeservice.entity.Customer;
import org.homeservice.repository.CustomerRepository;
import org.homeservice.repository.base.BaseRepositoryImpl;
import org.homeservice.util.HibernateUtil;
import org.homeservice.util.QueryUtil;

import java.util.Optional;

public class CustomerRepositoryImpl extends BaseRepositoryImpl<Customer, Long>
        implements CustomerRepository {
    private static CustomerRepository repository;

    private CustomerRepositoryImpl() {
    }

    @Override
    public Optional<Customer> findByUsername(String username) {
        String query = "select c from Customer as c where c.username = :username";
        return Optional.ofNullable(QueryUtil.getSingleResult(HibernateUtil.getCurrentEntityManager()
                .createQuery(query,Customer.class).setParameter("username",username)));
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        String query = "select c from Customer as c where c.email = :email";
        return Optional.ofNullable(QueryUtil.getSingleResult(HibernateUtil.getCurrentEntityManager()
                .createQuery(query,Customer.class).setParameter("email",email)));
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
