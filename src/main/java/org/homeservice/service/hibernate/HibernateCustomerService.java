package org.homeservice.service.hibernate;

import org.homeservice.entity.*;
import org.homeservice.service.hibernate.base.HibernateBaseService;

import java.time.LocalDateTime;
import java.util.List;

public interface HibernateCustomerService extends HibernateBaseService<Customer, Long> {
    Customer save(String firstName, String lastName, String username, String email, String password);

    Order saveOrder(String description, Double offerPrice, LocalDateTime workingTime, String address, Long subServiceId);

    List<Specialist> findAllVerifiedSpecialist();

    List<Service> loadAllServices();

    List<SubService> loadAllSubServices();

    List<SubService> loadSubServicesOfService(Long serviceId);

    Rate saveRate(Long customerId, Integer score, String comment, Long orderId);

    Rate saveRate(Long customerId, Integer score, Long orderId);

    void changePassword(Long id, String oldPassword, String newPassword);

    boolean isExistedUsername(String username);

    boolean isExistedEmail(String email);
}
