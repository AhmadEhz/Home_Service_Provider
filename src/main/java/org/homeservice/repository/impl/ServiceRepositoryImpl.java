package org.homeservice.repository.impl;

import org.homeservice.entity.Service;
import org.homeservice.repository.ServiceRepository;
import org.homeservice.repository.base.BaseRepositoryImpl;

public class ServiceRepositoryImpl extends BaseRepositoryImpl<Service, Long> implements ServiceRepository {
    private static ServiceRepository repository;

    private ServiceRepositoryImpl() {
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
