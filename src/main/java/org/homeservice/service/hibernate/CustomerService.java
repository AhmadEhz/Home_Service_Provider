package org.homeservice.service.hibernate;

import org.homeservice.entity.*;
import org.homeservice.service.hibernate.base.BaseService;

import java.time.LocalDateTime;
import java.util.List;

public interface CustomerService extends BaseService<Customer, Long> {
    Customer save(String firstName, String lastName, String username, String email, String password);

    Order saveOrder(String description, Double offerPrice, LocalDateTime workingTime, String address, Long subServiceId);

    List<Specialist> findAllVerifiedSpecialist();

    List<Service> loadAllServices();

    List<SubService> loadAllSubServices();

    List<SubService> loadSubServicesOfService(Long serviceId);

    Rate saveRate(Long customerId, Double score, String comment, Long orderId);

    Rate saveRate(Long customerId, Double score, Long orderId);

    void changePassword(Long id, String oldPassword, String newPassword);

    boolean isExistedUsername(String username);

    boolean isExistedEmail(String email);
}
