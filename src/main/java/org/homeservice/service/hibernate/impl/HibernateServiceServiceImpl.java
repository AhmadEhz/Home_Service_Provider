package org.homeservice.service.hibernate.impl;

import org.homeservice.entity.Service;
import org.homeservice.repository.hibernate.HibernateServiceRepository;
import org.homeservice.repository.hibernate.impl.HibernateServiceRepositoryImpl;
import org.homeservice.service.hibernate.HibernateServiceService;
import org.homeservice.service.hibernate.base.HibernateBaseServiceImpl;

public class HibernateServiceServiceImpl extends HibernateBaseServiceImpl<Service, Long, HibernateServiceRepository> implements HibernateServiceService {
    private static HibernateServiceService service;

    private HibernateServiceServiceImpl() {
        super(HibernateServiceRepositoryImpl.getRepository());
    }

    public static HibernateServiceService getService() {
        if (service == null)
            service = new HibernateServiceServiceImpl();
        return service;
    }
}
