package org.homeservice.service.impl;

import org.homeservice.entity.Specialist;
import org.homeservice.repository.SpecialistRepository;
import org.homeservice.repository.impl.SpecialistRepositoryImpl;
import org.homeservice.service.SpecialistService;
import org.homeservice.service.base.BaseServiceImpl;

public class SpecialistServiceImpl extends BaseServiceImpl<Specialist, Long, SpecialistRepository>
        implements SpecialistService {
    private static SpecialistService service;

    protected SpecialistServiceImpl() {
        super(SpecialistRepositoryImpl.getRepository());
    }

    public static SpecialistService getService() {
        if (service == null)
            service = new SpecialistServiceImpl();
        return service;
    }
}
