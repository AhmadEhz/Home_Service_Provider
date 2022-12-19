package org.homeservice.repository.hibernate.impl;

import org.homeservice.entity.Rate;
import org.homeservice.repository.hibernate.HibernateRateRepository;
import org.homeservice.repository.hibernate.base.HibernateBaseRepositoryImpl;

public class HibernateRateRepositoryImpl extends HibernateBaseRepositoryImpl<Rate, Long> implements HibernateRateRepository {
    private static HibernateRateRepository repository;

    private HibernateRateRepositoryImpl() {
    }

    @Override
    protected Class<Rate> getEntityClass() {
        return Rate.class;
    }

    public static HibernateRateRepository getRepository() {
        if (repository == null)
            repository = new HibernateRateRepositoryImpl();
        return repository;
    }

}
