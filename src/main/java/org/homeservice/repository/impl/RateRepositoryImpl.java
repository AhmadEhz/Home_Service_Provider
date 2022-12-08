package org.homeservice.repository.impl;

import org.homeservice.entity.Rate;
import org.homeservice.repository.RateRepository;
import org.homeservice.repository.base.BaseRepositoryImpl;
import org.homeservice.util.HibernateUtil;
import org.homeservice.util.QueryUtil;

public class RateRepositoryImpl extends BaseRepositoryImpl<Rate, Long> implements RateRepository {
    private static RateRepository repository;

    private RateRepositoryImpl() {
    }

    @Override
    protected Class<Rate> getEntityClass() {
        return Rate.class;
    }

    public static RateRepository getRepository() {
        if (repository == null)
            repository = new RateRepositoryImpl();
        return repository;
    }

}
