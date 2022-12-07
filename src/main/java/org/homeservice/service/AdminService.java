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

    void verifySpecialist(Long specialistId);

    List<Specialist> loadNewSpecialists();
}
