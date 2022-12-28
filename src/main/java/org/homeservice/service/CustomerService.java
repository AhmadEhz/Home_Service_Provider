package org.homeservice.service;

import org.homeservice.entity.Customer;
import org.homeservice.service.base.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface CustomerService extends BaseService<Customer, Long> {
    void changePassword(String username, String oldPassword, String newPassword);

    List<Customer> loadAllByFilter(Map<String, String> filters);

    boolean isExistedUsername(String username);

    boolean isExistedEmail(String email);

    void deleteByAdmin(Customer customer);
}
