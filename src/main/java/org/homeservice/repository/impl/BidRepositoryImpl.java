package org.homeservice.repository.impl;

import org.homeservice.entity.Bid;
import org.homeservice.repository.BidRepository;
import org.homeservice.repository.base.BaseRepositoryImpl;

public class BidRepositoryImpl extends BaseRepositoryImpl<Bid,Long> implements BidRepository {
    private static BidRepository repository;
    private BidRepositoryImpl(){
    }
    @Override
    protected Class<Bid> getEntityClass() {
        return Bid.class;
    }
    public static BidRepository getRepository() {
        if(repository==null)
            repository = new BidRepositoryImpl();
        return repository;
    }
}