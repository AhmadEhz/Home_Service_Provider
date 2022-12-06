package org.homeservice.service.impl;

import org.homeservice.entity.*;
import org.homeservice.repository.*;
import org.homeservice.repository.impl.*;
import org.homeservice.service.AdminService;
import org.homeservice.service.base.BaseServiceImpl;

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
    public Service addService(String name, SubService... subServices) {
        Service service = new Service(name);
        for (SubService s : subServices) {
            service.addSubService(s);
        }
        serviceRepository.save(service);
        return service;
    }

    public SubService addSubService(String name, String description, Double basePrice, Long serviceId) {
        SubService subService = new SubService(name, description, basePrice, new Service(serviceId));
        executeUpdate(() -> subServiceRepository.save(subService));
        return subService;
    }

    public static AdminService getAdminService() {
        if (adminService == null)
            adminService = new AdminServiceImpl();
        return adminService;
    }
}
