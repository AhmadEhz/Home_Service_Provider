package org.homeservice.service.impl;

import org.homeservice.entity.Service;
import org.homeservice.repository.ServiceRepository;
import org.homeservice.service.ServiceService;
import org.homeservice.service.base.BaseServiceImpl;
import org.springframework.context.annotation.Scope;

@org.springframework.stereotype.Service
@Scope("singleton")
public class ServiceServiceImpl extends BaseServiceImpl<Service,Long, ServiceRepository> implements ServiceService {
    public ServiceServiceImpl(ServiceRepository repository) {
        super(repository);
    }
}
