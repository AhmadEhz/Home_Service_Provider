package org.homeservice.service.hibernate.impl;

import org.homeservice.entity.*;
import org.homeservice.repository.hibernate.*;
import org.homeservice.repository.hibernate.impl.*;
import org.homeservice.service.hibernate.*;
import org.homeservice.service.hibernate.base.BaseServiceImpl;
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
    public List<Service> loadAllServices() {
        return serviceService.loadAll();
    }

    @Override
    public List<SubService> loadAllSubServices() {
        return subServiceService.loadAll();
    }

    @Override
    public List<SubService> loadSubServiceOfService(Long serviceId) {
        return subServiceService.loadAll(serviceId);
    }

    @Override
    public void addSpecialistToSubService(Long specialistId, Long subServiceId) {
        Optional<Specialist> optionalSpecialist = specialistService.loadById(specialistId);
        Optional<SubService> optionalSubService = subServiceService.loadById(subServiceId);

        if (optionalSpecialist.isEmpty())
            throw new NotFoundException("Specialist not found.");
        if (optionalSubService.isEmpty())
            throw new NotFoundException("SubService not found.");

        checkSpecialistStatus(optionalSpecialist.get());
        optionalSpecialist.get().addSubService(optionalSubService.get());
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

    @Override
    public List<Specialist> loadNewSpecialists() {
        return specialistService.loadNewSpecialists();
    }

    @Override
    public List<Specialist> loadVerifiedSpecialists() {
        return specialistService.loadVerifiedSpecialists();
    }

    @Override
    public void editSubServiceBasePrice(double price, Long subServiceId) {
        subServiceService.editBasePrice(price, subServiceId);
    }

    @Override
    public void editSubServiceDescription(String description, Long subServiceId) {
        subServiceService.editDescription(description, subServiceId);
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

    private void checkSpecialistStatus(Specialist specialist) throws NotVerifiedException {
        if (specialist.getStatus() != SpecialistStatus.ACCEPTED)
            throw new NotVerifiedException("This specialist is not verified");
    }

    public static AdminService getAdminService() {
        if (adminService == null)
            adminService = new AdminServiceImpl();
        return adminService;
    }
}
