package org.homeservice.repository.hibernate.impl;

import org.homeservice.entity.Admin;
import org.homeservice.repository.hibernate.HibernateAdminRepository;
import org.homeservice.repository.hibernate.base.HibernateBaseRepositoryImpl;

public class HibernateAdminRepositoryImpl extends HibernateBaseRepositoryImpl<Admin, Long> implements HibernateAdminRepository {
    private static HibernateAdminRepository repository;

    private HibernateAdminRepositoryImpl() {
    }

    @Override
    protected Class<Admin> getEntityClass() {
        return Admin.class;
    }

    public static HibernateAdminRepository getRepository() {
        if (repository == null)
            repository = new HibernateAdminRepositoryImpl();
        return repository;
    }
}
