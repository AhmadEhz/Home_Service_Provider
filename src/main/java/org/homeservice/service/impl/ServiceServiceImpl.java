package org.homeservice.service.impl;

import org.homeservice.entity.Service;
import org.homeservice.repository.ServiceRepository;
import org.homeservice.service.ServiceService;
import org.homeservice.service.base.BaseServiceImpl;
import org.homeservice.util.exception.NonUniqueException;
import org.springframework.context.annotation.Scope;

import java.util.Optional;

@org.springframework.stereotype.Service
@Scope("singleton")
public class ServiceServiceImpl extends BaseServiceImpl<Service, Long, ServiceRepository> implements ServiceService {
    public ServiceServiceImpl(ServiceRepository repository) {
        super(repository);
    }

    @Override
    public void save(Service service) {
        if (isExistedName(service.getName()))
            throw new NonUniqueException("Service name is exist.");
        super.save(service);
    }

    @Override
    public Optional<Service> findByName(String name) {
        return repository.findServiceByName(name);
    }

    @Override
    public boolean isExistedName(String name) {
        return findByName(name).isPresent();
    }
}
