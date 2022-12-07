package org.homeservice.repository.impl;

import jakarta.persistence.TypedQuery;
import org.homeservice.entity.Service;
import org.homeservice.repository.ServiceRepository;
import org.homeservice.repository.base.BaseRepositoryImpl;
import org.homeservice.util.HibernateUtil;
import org.homeservice.util.QueryUtil;

import java.util.Optional;

public class ServiceRepositoryImpl extends BaseRepositoryImpl<Service, Long> implements ServiceRepository {
    private static ServiceRepository repository;

    private ServiceRepositoryImpl() {
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

    public static ServiceRepository getRepository() {
        if (repository == null)
            repository = new ServiceRepositoryImpl();
        return repository;
    }
}
