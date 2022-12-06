package org.homeservice.repository.impl;

import org.homeservice.entity.SubService;
import org.homeservice.repository.SubServiceRepository;
import org.homeservice.repository.base.BaseRepositoryImpl;

public class SubServiceRepositoryImpl extends BaseRepositoryImpl<SubService, Long> implements SubServiceRepository {

    private static SubServiceRepository repository;

    private SubServiceRepositoryImpl() {
    }

    @Override
    protected Class<SubService> getEntityClass() {
        return SubService.class;
    }

    public static SubServiceRepository getRepository() {
        if (repository == null)
            repository = new SubServiceRepositoryImpl();
        return repository;
    }
}
