package org.homeservice.service.hibernate.impl;

import org.homeservice.entity.*;
import org.homeservice.repository.hibernate.*;
import org.homeservice.repository.hibernate.impl.*;
import org.homeservice.service.hibernate.*;
import org.homeservice.service.hibernate.base.HibernateBaseServiceImpl;
import org.homeservice.util.exception.CustomIllegalArgumentException;
import org.homeservice.util.exception.NotFoundException;
import org.homeservice.util.exception.NotVerifiedException;

import java.util.List;
import java.util.Optional;

public class HibernateAdminServiceImpl extends HibernateBaseServiceImpl<Admin, Long, HibernateAdminRepository> implements HibernateAdminService {
    private static HibernateAdminService adminService;
    private final HibernateServiceRepository serviceRepository;
    private final HibernateSubServiceRepository subServiceRepository;
    private final HibernateSpecialistRepository specialistRepository;
    private final HibernateServiceService serviceService;
    private final HibernateSubServiceService subServiceService;
    private final HibernateSpecialistService specialistService;
    private final HibernateSubServiceSpecialistService subServiceSpecialistService;

    private HibernateAdminServiceImpl() {
        super(HibernateAdminRepositoryImpl.getRepository());
        serviceRepository = HibernateServiceRepositoryImpl.getRepository();
        subServiceRepository = HibernateSubServiceRepositoryImpl.getRepository();
        specialistRepository = HibernateSpecialistRepositoryImpl.getRepository();
        specialistService = HibernateSpecialistServiceImpl.getService();
        serviceService = HibernateServiceServiceImpl.getService();
        subServiceService = HibernateSubServiceServiceImpl.getService();
        subServiceSpecialistService = HibernateSubServiceSpecialistServiceImpl.getService();
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
        subServiceSpecialistService.save(optionalSpecialist.get().getId(), optionalSubService.get().getId());
    }

    @Override
    public void removeSpecialistFromSubService(Long specialistId, Long subServiceId) {
        Optional<Specialist> optionalSpecialist = specialistService.loadById(specialistId);
        Optional<SubService> optionalSubService = subServiceService.loadById(subServiceId);
        if (optionalSpecialist.isEmpty())
            throw new NotFoundException("Specialist not found.");
        if (optionalSubService.isEmpty())
            throw new NotFoundException("SubService not found");
        subServiceSpecialistService.remove(optionalSpecialist.get().getId(), optionalSubService.get().getId());
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

    public static HibernateAdminService getAdminService() {
        if (adminService == null)
            adminService = new HibernateAdminServiceImpl();
        return adminService;
    }
}
