package org.homeservice.service.hibernate.impl;

import org.homeservice.entity.Service;
import org.homeservice.repository.hibernate.ServiceRepository;
import org.homeservice.repository.hibernate.impl.ServiceRepositoryImpl;
import org.homeservice.service.hibernate.ServiceService;
import org.homeservice.service.hibernate.base.BaseServiceImpl;

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
