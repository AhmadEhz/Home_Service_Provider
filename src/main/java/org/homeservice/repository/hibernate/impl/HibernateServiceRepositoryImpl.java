package org.homeservice.repository.hibernate.impl;

import jakarta.persistence.TypedQuery;
import org.homeservice.entity.Service;
import org.homeservice.repository.hibernate.HibernateServiceRepository;
import org.homeservice.repository.hibernate.base.HibernateBaseRepositoryImpl;
import org.homeservice.util.HibernateUtil;
import org.homeservice.util.QueryUtil;

import java.util.Optional;

public class HibernateServiceRepositoryImpl extends HibernateBaseRepositoryImpl<Service, Long> implements HibernateServiceRepository {
    private static HibernateServiceRepository repository;

    private HibernateServiceRepositoryImpl() {
    }

    @Override
    public Optional<Service> findByName(String name) {
        String query = "select s from Service as s where s.name =:name";
        TypedQuery<Service> typedQuery = HibernateUtil.getCurrentEntityManager()
                .createQuery(query, Service.class).setParameter("name", name);
        return Optional.ofNullable(QueryUtil.getSingleResult(typedQuery));
    }

    @Override
    protected Class<Service> getEntityClass() {
        return Service.class;
    }

    public static HibernateServiceRepository getRepository() {
        if (repository == null)
            repository = new HibernateServiceRepositoryImpl();
        return repository;
    }
}
