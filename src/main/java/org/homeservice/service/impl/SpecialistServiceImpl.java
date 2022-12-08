package org.homeservice.service.impl;

import org.homeservice.entity.*;
import org.homeservice.repository.SpecialistRepository;
import org.homeservice.repository.impl.SpecialistRepositoryImpl;
import org.homeservice.service.*;
import org.homeservice.service.base.BaseServiceImpl;
import org.homeservice.util.exception.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SpecialistServiceImpl extends BaseServiceImpl<Specialist, Long, SpecialistRepository>
        implements SpecialistService {
    private static SpecialistService service;
    private final ServiceService serviceService;
    private final SubServiceService subServiceService;

    private SpecialistServiceImpl() {
        super(SpecialistRepositoryImpl.getRepository());
        serviceService = ServiceServiceImpl.getService();
        subServiceService = SubServiceServiceImpl.getService();
    }

    @Override
    public List<Specialist> loadNewSpecialists() {
        return repository.findAll(SpecialistStatus.NEW);
    }

    @Override
    public List<Specialist> loadVerifiedSpecialists() {
        return repository.findAll(SpecialistStatus.ACCEPTED);
    }

    @Override
    public void changeStatus(Long specialistId, SpecialistStatus status) {
        int update = repository.changeStatus(specialistId, status);
        if (update < 1)
            throw new CustomIllegalArgumentException("Specialist with this id not found.");
    }

    @Override
    public void updateScore(Long id) {
        AtomicInteger update = new AtomicInteger();
        executeUpdate(() -> update.set(repository.updateScore(id)));
        if (update.get() < 1)
            throw new NotFoundException("Specialist not found.");
    }

    public static SpecialistService getService() {
        if (service == null)
            service = new SpecialistServiceImpl();
        return service;
    }
}
