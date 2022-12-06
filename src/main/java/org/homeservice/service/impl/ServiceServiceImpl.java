package org.homeservice.service.impl;

import org.homeservice.entity.Service;
import org.homeservice.repository.ServiceRepository;
import org.homeservice.repository.impl.ServiceRepositoryImpl;
import org.homeservice.service.ServiceService;
import org.homeservice.service.base.BaseServiceImpl;

public class ServiceServiceImpl extends BaseServiceImpl<Service, Long, ServiceRepository> implements ServiceService {
    private static ServiceService service;

    private ServiceServiceImpl() {
        super(ServiceRepositoryImpl.getRepository());
    }

    public static ServiceService getService() {
        if (service == null)
            service = new ServiceServiceImpl();
        return service;
    }
}
