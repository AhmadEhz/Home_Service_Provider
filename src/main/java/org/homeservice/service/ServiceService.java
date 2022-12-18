package org.homeservice.service;

import org.homeservice.entity.Service;
import org.homeservice.service.base.BaseService;

import java.util.Optional;

@org.springframework.stereotype.Service
public interface ServiceService extends BaseService<Service, Long> {
    Optional<Service> findByName(String name);

    boolean isExistedName(String name);
}
