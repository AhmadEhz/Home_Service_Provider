package org.homeservice.repository.hibernate.impl;

import org.homeservice.entity.Bid;
import org.homeservice.repository.hibernate.HibernateBidRepository;
import org.homeservice.repository.hibernate.base.HibernateBaseRepositoryImpl;

public class HibernateBidRepositoryImpl extends HibernateBaseRepositoryImpl<Bid,Long> implements HibernateBidRepository {
    private static HibernateBidRepository repository;
    private HibernateBidRepositoryImpl(){
    }
    @Override
    protected Class<Bid> getEntityClass() {
        return Bid.class;
    }
    public static HibernateBidRepository getRepository() {
        if(repository==null)
            repository = new HibernateBidRepositoryImpl();
        return repository;
    }
}
