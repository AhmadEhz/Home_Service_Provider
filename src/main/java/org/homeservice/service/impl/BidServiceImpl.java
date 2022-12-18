package org.homeservice.service.impl;

import org.homeservice.entity.Bid;
import org.homeservice.repository.BidRepository;
import org.homeservice.service.base.BaseServiceImpl;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
public class BidServiceImpl extends BaseServiceImpl<Bid, Long, BidRepository> {
    protected BidServiceImpl(BidRepository repository) {
        super(repository);
    }
}
