package org.homeservice.service.impl;

import org.homeservice.entity.Service;
import org.homeservice.repository.ServiceRepository;
import org.homeservice.service.ServiceService;
import org.homeservice.service.base.BaseServiceImpl;

public class ServiceServiceImpl extends BaseServiceImpl<Service,Long, ServiceRepository> implements ServiceService {
    protected ServiceServiceImpl(ServiceRepository repository) {
        super(repository);
    }
}
