package org.homeservice.service.impl;

import org.homeservice.entity.*;
import org.homeservice.repository.SpecialistRepository;
import org.homeservice.repository.impl.SpecialistRepositoryImpl;
import org.homeservice.service.*;
import org.homeservice.service.base.BaseServiceImpl;
import org.homeservice.util.exception.*;

import java.util.List;
import java.util.Optional;

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

    //todo: fill exception messages.
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
    public void addToService(Long specialistId, Long serviceId) {
        Optional<Specialist> optionalSpecialist = loadById(specialistId);
        Optional<Service> optionalService = serviceService.loadById(serviceId);
        if (optionalSpecialist.isEmpty())
            throw new CustomIllegalArgumentException();
        if (optionalService.isEmpty())
            throw new CustomIllegalArgumentException();

        optionalSpecialist.get().addService(optionalService.get());
        executeUpdate(() -> repository.update(optionalSpecialist.get()));
    }

    @Override
    public void addToSubService(Long specialistId, Long subServiceId) {
        Optional<Specialist> optionalSpecialist = loadById(specialistId);
        Optional<SubService> optionalSubService = subServiceService.loadById(subServiceId);

        if (optionalSpecialist.isEmpty())
            throw new NotFoundException();
        if (optionalSubService.isEmpty())
            throw new NotFoundException();

        checkSpecialistStatus(optionalSpecialist.get());
        optionalSpecialist.get().addSubService(optionalSubService.get());
        executeUpdate(() -> repository.update(optionalSpecialist.get()));
    }

    @Override
    public void removeFromService(Long specialistId, Long serviceId) {
        Optional<Specialist> optionalSpecialist = loadById(specialistId);
        Optional<Service> optionalService = serviceService.loadById(serviceId);
        if (optionalSpecialist.isEmpty())
            throw new CustomIllegalArgumentException();
        if (optionalService.isEmpty())
            throw new CustomIllegalArgumentException();

        checkSpecialistStatus(optionalSpecialist.get());
        optionalSpecialist.get().removeService(optionalService.get());
        removeSubServicesFromSpecialist(optionalSpecialist.get(), optionalService.get());
        executeUpdate(() -> repository.update(optionalSpecialist.get()));
    }

    @Override
    public void removeFromSubService(Long specialistId, Long subServiceId) {
        Optional<Specialist> optionalSpecialist = loadById(specialistId);
        Optional<SubService> optionalService = subServiceService.loadById(subServiceId);
        if (optionalSpecialist.isEmpty())
            throw new CustomIllegalArgumentException();
        if (optionalService.isEmpty())
            throw new CustomIllegalArgumentException();

        optionalSpecialist.get().removeSubService(optionalService.get());
        executeUpdate(() -> repository.update(optionalSpecialist.get()));
    }

    private void removeSubServicesFromSpecialist(Specialist specialist, Service service) {
        for (SubService subService : service.getSubServices()) {
            specialist.removeSubService(subService);
        }
    }

    private void checkSpecialistStatus(Specialist specialist) throws NotVerifiedException {
        if (specialist.getStatus() != SpecialistStatus.ACCEPTED)
            throw new NotVerifiedException("This specialist is not verified");
    }

    public static SpecialistService getService() {
        if (service == null)
            service = new SpecialistServiceImpl();
        return service;
    }
}
