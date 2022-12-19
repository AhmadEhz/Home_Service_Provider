package org.homeservice.service.hibernate;

import org.homeservice.entity.Admin;
import org.homeservice.entity.Service;
import org.homeservice.entity.Specialist;
import org.homeservice.entity.SubService;
import org.homeservice.service.hibernate.base.HibernateBaseService;

import java.util.List;

public interface HibernateAdminService extends HibernateBaseService<Admin,Long> {
    Service saveService(String name);
    Service saveService(String name, SubService... subServices);

    SubService saveSubService(String name, String description, Double basePrice, Long serviceId);

    SubService saveSubService(String name, String description, Long serviceId);

    List<Service> loadAllServices();

    List<SubService> loadAllSubServices();

    List<SubService> loadSubServiceOfService(Long serviceId);

    void addSpecialistToSubService(Long specialistId, Long subServiceId);

    void removeSpecialistFromSubService(Long specialistId, Long subServiceId);

    void verifySpecialist(Long specialistId);

    List<Specialist> loadNewSpecialists();

    List<Specialist> loadVerifiedSpecialists();

    void editSubServiceBasePrice(double price, Long subServiceId);

    void editSubServiceDescription(String description, Long subServiceId);

    void changePassword(Long id, String oldPassword, String newPassword);
}
