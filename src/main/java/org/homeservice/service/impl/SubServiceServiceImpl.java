package org.homeservice.service.impl;

import org.homeservice.entity.SubService;
import org.homeservice.repository.SubServiceRepository;
import org.homeservice.repository.impl.SubServiceRepositoryImpl;
import org.homeservice.service.SubServiceService;
import org.homeservice.service.base.BaseServiceImpl;

public class SubServiceServiceImpl extends BaseServiceImpl<SubService, Long, SubServiceRepository>
        implements SubServiceService {
    private static SubServiceService service;

    private SubServiceServiceImpl() {
        super(SubServiceRepositoryImpl.getRepository());
    }

    public static SubServiceService getService() {
        if (service == null)
            service = new SubServiceServiceImpl();
        return service;
    }
}
