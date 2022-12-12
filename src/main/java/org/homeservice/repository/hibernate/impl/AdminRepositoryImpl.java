package org.homeservice.repository.hibernate.impl;

import org.homeservice.entity.Admin;
import org.homeservice.repository.hibernate.AdminRepository;
import org.homeservice.repository.hibernate.base.BaseRepositoryImpl;

public class AdminRepositoryImpl extends BaseRepositoryImpl<Admin, Long> implements AdminRepository {
    private static AdminRepository repository;

    private AdminRepositoryImpl() {
    }

    @Override
    protected Class<Admin> getEntityClass() {
        return Admin.class;
    }

    public static AdminRepository getRepository() {
        if (repository == null)
            repository = new AdminRepositoryImpl();
        return repository;
    }
}
