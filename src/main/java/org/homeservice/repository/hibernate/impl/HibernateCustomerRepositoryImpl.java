package org.homeservice.repository.hibernate.impl;

import org.homeservice.entity.Customer;
import org.homeservice.repository.hibernate.HibernateCustomerRepository;
import org.homeservice.repository.hibernate.base.HibernateBaseRepositoryImpl;
import org.homeservice.util.HibernateUtil;
import org.homeservice.util.QueryUtil;

import java.util.Optional;

public class HibernateCustomerRepositoryImpl extends HibernateBaseRepositoryImpl<Customer, Long>
        implements HibernateCustomerRepository {
    private static HibernateCustomerRepository repository;

    private HibernateCustomerRepositoryImpl() {
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

    public static HibernateCustomerRepository getRepository() {
        if (repository == null)
            repository = new HibernateCustomerRepositoryImpl();
        return repository;
    }
}
