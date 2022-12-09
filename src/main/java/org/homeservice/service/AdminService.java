package org.homeservice.service;

import org.homeservice.entity.Admin;
import org.homeservice.entity.Service;
import org.homeservice.entity.Specialist;
import org.homeservice.entity.SubService;
import org.homeservice.service.base.BaseService;

import java.util.List;

public interface AdminService extends BaseService<Admin,Long> {
    Service saveService(String name);
    Service saveService(String name, SubService... subServices);

    SubService saveSubService(String name, String description, Double basePrice, Long serviceId);

    SubService saveSubService(String name, String description, Long serviceId);

    void addSpecialistToService(Long specialistId, Long serviceId);

    void addSpecialistToSubService(Long specialistId, Long subServiceId);

    void removeSpecialistFromService(Long specialistId, Long serviceId);

    void removeSpecialistFromSubService(Long specialistId, Long subServiceId);

    void verifySpecialist(Long specialistId);

    List<Specialist> loadNewSpecialists();

    void changePassword(Long id, String oldPassword, String newPassword);
}
