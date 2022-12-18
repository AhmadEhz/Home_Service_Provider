package org.homeservice.service.impl;

import org.homeservice.entity.Bid;
import org.homeservice.repository.BidRepository;
import org.homeservice.service.base.BaseServiceImpl;

public class BidServiceImpl extends BaseServiceImpl<Bid, Long, BidRepository> {
    protected BidServiceImpl(BidRepository repository) {
        super(repository);
    }
}
