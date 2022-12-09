package org.homeservice.service.impl;

import org.homeservice.entity.*;
import org.homeservice.repository.*;
import org.homeservice.repository.impl.*;
import org.homeservice.service.AdminService;
import org.homeservice.service.ServiceService;
import org.homeservice.service.SpecialistService;
import org.homeservice.service.SubServiceService;
import org.homeservice.service.base.BaseServiceImpl;
import org.homeservice.util.exception.CustomIllegalArgumentException;
import org.homeservice.util.exception.NotFoundException;
import org.homeservice.util.exception.NotVerifiedException;

import java.util.List;
import java.util.Optional;

public class AdminServiceImpl extends BaseServiceImpl<Admin, Long, AdminRepository> implements AdminService {
    private static AdminService adminService;
    private final ServiceRepository serviceRepository;
    private final SubServiceRepository subServiceRepository;
    private final SpecialistRepository specialistRepository;
    private final ServiceService serviceService;
    private final SubServiceService subServiceService;
    private final SpecialistService specialistService;

    private AdminServiceImpl() {
        super(AdminRepositoryImpl.getRepository());
        serviceRepository = ServiceRepositoryImpl.getRepository();
        subServiceRepository = SubServiceRepositoryImpl.getRepository();
        specialistRepository = SpecialistRepositoryImpl.getRepository();
        specialistService = SpecialistServiceImpl.getService();
        serviceService = ServiceServiceImpl.getService();
        subServiceService = SubServiceServiceImpl.getService();
    }

    @Override
    public Service saveService(String name) {
        return saveService(name, null);
    }

    @Override
    public Service saveService(String name, SubService... subServices) {
        if (serviceRepository.findByName(name).isPresent())
            throw new CustomIllegalArgumentException("This service is exist.");

        Service service = new Service(name);
        for (SubService s : subServices) {
            service.addSubService(s);
        }
        checkEntity(service);
        serviceRepository.save(service);
        return service;
    }

    @Override
    public SubService saveSubService(String name, String description, Double basePrice, Long serviceId) {
        Optional<Service> optionalService = serviceService.loadById(serviceId);
        if (optionalService.isEmpty())
            throw new NotFoundException("Service not found.");
        SubService subService = new SubService(name, description, basePrice, optionalService.get());
        checkEntity(subService);
        executeUpdate(() -> subServiceRepository.save(subService));
        return subService;
    }

    @Override
    public SubService saveSubService(String name, String description, Long serviceId) {
        return saveSubService(name, description, null, serviceId);
    }

    @Override
    public void addSpecialistToService(Long specialistId, Long serviceId) {
        Optional<Specialist> optionalSpecialist = specialistService.loadById(specialistId);
        Optional<Service> optionalService = serviceService.loadById(serviceId);
        if (optionalSpecialist.isEmpty())
            throw new NotFoundException("Specialist not found.");
        if (optionalService.isEmpty())
            throw new CustomIllegalArgumentException("Service not found.");

        optionalSpecialist.get().addService(optionalService.get());
        executeUpdate(() -> specialistRepository.update(optionalSpecialist.get()));
    }

    @Override
    public void addSpecialistToSubService(Long specialistId, Long subServiceId) {
        Optional<Specialist> optionalSpecialist = specialistService.loadById(specialistId);
        Optional<SubService> optionalSubService = subServiceService.loadById(subServiceId);

        if (optionalSpecialist.isEmpty())
            throw new NotFoundException("Specialist not found.");
        if (optionalSubService.isEmpty())
            throw new NotFoundException("SubService not found.");
        boolean containsThisService = false;
        for (ServiceSpecialist sp : optionalSpecialist.get().getServices()) {
            if (sp.getService().equals(optionalSubService.get().getService())) {
                containsThisService = true;
                break;
            }
        }
        if (!containsThisService)
            throw new IllegalArgumentException
                    ("Specialist is not in this service. you must first add it to this service.");

        checkSpecialistStatus(optionalSpecialist.get());
        optionalSpecialist.get().addSubService(optionalSubService.get());
        executeUpdate(() -> specialistRepository.update(optionalSpecialist.get()));
    }

    @Override
    public void removeSpecialistFromService(Long specialistId, Long serviceId) {
        Optional<Specialist> optionalSpecialist = specialistService.loadById(specialistId);
        Optional<Service> optionalService = serviceService.loadById(serviceId);
        if (optionalSpecialist.isEmpty())
            throw new NotFoundException("Specialist not found.");
        if (optionalService.isEmpty())
            throw new NotFoundException("Service not found.");

        checkSpecialistStatus(optionalSpecialist.get());
        optionalSpecialist.get().removeService(optionalService.get());
        removeSubServicesFromSpecialist(optionalSpecialist.get(), optionalService.get());
        executeUpdate(() -> specialistRepository.update(optionalSpecialist.get()));
    }

    @Override
    public void removeSpecialistFromSubService(Long specialistId, Long subServiceId) {
        Optional<Specialist> optionalSpecialist = specialistService.loadById(specialistId);
        Optional<SubService> optionalSubService = subServiceService.loadById(subServiceId);
        if (optionalSpecialist.isEmpty())
            throw new NotFoundException("Specialist not found.");
        if (optionalSubService.isEmpty())
            throw new NotFoundException("SubService not found");

        optionalSpecialist.get().removeSubService(optionalSubService.get());
        executeUpdate(() -> specialistRepository.update(optionalSpecialist.get()));
    }

    @Override
    public void verifySpecialist(Long specialistId) {
        specialistService.changeStatus(specialistId, SpecialistStatus.ACCEPTED);
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

    @Override
    public List<Specialist> loadNewSpecialists() {
        return specialistService.loadNewSpecialists();
    }

    @Override
    public void changePassword(Long id, String oldPassword, String newPassword) {
        Optional<Admin> optionalAdmin = loadById(id);
        if (optionalAdmin.isEmpty())
            throw new NotFoundException("Admin not found.");
        if (optionalAdmin.get().getPassword().equals(oldPassword))
            throw new CustomIllegalArgumentException("Password is not match");

        optionalAdmin.get().setPassword(newPassword);
        executeUpdate(() -> repository.update(optionalAdmin.get()));
    }

    public static AdminService getAdminService() {
        if (adminService == null)
            adminService = new AdminServiceImpl();
        return adminService;
    }
}
