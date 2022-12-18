package org.homeservice.service.impl;

import org.homeservice.entity.Customer;
import org.homeservice.repository.CustomerRepository;
import org.homeservice.service.CustomerService;
import org.homeservice.service.base.BaseServiceImpl;
import org.homeservice.util.exception.CustomIllegalArgumentException;
import org.homeservice.util.exception.NotFoundException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
public class CustomerServiceImpl extends BaseServiceImpl<Customer, Long, CustomerRepository>
        implements CustomerService {
    public CustomerServiceImpl(CustomerRepository repository) {
        super(repository);
    }

    @Override
    public void changePassword(String username, String oldPassword, String newPassword) {
        Customer customer = repository.findCustomerByUsername(username).
                orElseThrow(() -> new NotFoundException("Customer not found."));
        if (!customer.getPassword().equals(oldPassword))
            throw new CustomIllegalArgumentException("Old password is incorrect.");
        customer.setPassword(newPassword);
        update(customer);
    }

    @Override
    public boolean isExistedUsername(String username) {
        return repository.findCustomerByUsername(username).isPresent();
    }

    @Override
    public boolean isExistedEmail(String email) {
        return repository.findCustomerByEmail(email).isPresent();
    }
}
