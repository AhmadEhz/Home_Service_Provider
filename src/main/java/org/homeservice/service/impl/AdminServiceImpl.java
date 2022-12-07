package org.homeservice.service.impl;

import org.homeservice.entity.*;
import org.homeservice.repository.*;
import org.homeservice.repository.impl.*;
import org.homeservice.service.AdminService;
import org.homeservice.service.base.BaseServiceImpl;
import org.homeservice.util.exception.CustomIllegalArgumentException;

import java.util.List;
import java.util.Optional;

public class AdminServiceImpl extends BaseServiceImpl<Admin, Long, AdminRepository> implements AdminService {
    private static AdminService adminService;
    private final ServiceRepository serviceRepository;
    private final SubServiceRepository subServiceRepository;

    private AdminServiceImpl() {
        super(AdminRepositoryImpl.getRepository());
        serviceRepository = ServiceRepositoryImpl.getRepository();
        subServiceRepository = SubServiceRepositoryImpl.getRepository();
    }

    @Override
    public Service saveService(String name){
        return saveService(name,null);
    }

    @Override
    public Service saveService(String name, SubService... subServices) {
        if(serviceRepository.findByName(name).isPresent())
            throw new CustomIllegalArgumentException("This service is exist.");

        Service service = new Service(name);
        for (SubService s : subServices) {
            service.addSubService(s);
        }
        serviceRepository.save(service);
        return service;
    }

    @Override
    public SubService saveSubService(String name, String description, Double basePrice, Long serviceId) {
        Optional<Service> optionalService = serviceRepository.findById(serviceId);
        if(optionalService.isEmpty())
            throw new CustomIllegalArgumentException("Service not found.");
        SubService subService = new SubService(name, description, basePrice, optionalService.get());
        executeUpdate(() -> subServiceRepository.save(subService));
        return subService;
    }

    @Override
    public List<Specialist> loadNewSpecialists() {
        return null;
    }

    public static AdminService getAdminService() {
        if (adminService == null)
            adminService = new AdminServiceImpl();
        return adminService;
    }
}
