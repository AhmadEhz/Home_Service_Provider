package org.homeservice.service.impl;

import org.homeservice.entity.Specialist;
import org.homeservice.entity.SpecialistStatus;
import org.homeservice.repository.SpecialistRepository;
import org.homeservice.repository.impl.SpecialistRepositoryImpl;
import org.homeservice.service.SpecialistService;
import org.homeservice.service.base.BaseServiceImpl;
import org.homeservice.util.exception.CustomIllegalArgumentException;

import java.util.List;

public class SpecialistServiceImpl extends BaseServiceImpl<Specialist, Long, SpecialistRepository>
        implements SpecialistService {
    private static SpecialistService service;

    private SpecialistServiceImpl() {
        super(SpecialistRepositoryImpl.getRepository());
    }

    @Override
    public List<Specialist> loadNewSpecialists() {
        return repository.findNewSpecialists();
    }

    @Override
    public void changeStatus(Long specialistId, SpecialistStatus status) {
        int update = repository.changeStatus(specialistId, status);
        if (update < 1)
            throw new CustomIllegalArgumentException("Specialist with this id not found.");
    }

    public static SpecialistService getService() {
        if (service == null)
            service = new SpecialistServiceImpl();
        return service;
    }
}
