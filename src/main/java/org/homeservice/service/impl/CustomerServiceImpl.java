package org.homeservice.service.impl;

import org.homeservice.entity.Customer;
import org.homeservice.repository.CustomerRepository;
import org.homeservice.service.CustomerService;
import org.homeservice.service.base.BaseServiceImpl;
import org.homeservice.util.QueryUtil;
import org.homeservice.util.exception.CustomIllegalArgumentException;
import org.homeservice.util.exception.NotFoundException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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
    public List<Customer> loadAllByFilter(Map<String ,String> filters){
        return repository.findAll(QueryUtil.setSpecification(filters));
    }

    @Override
    public boolean isExistedUsername(String username) {
        return repository.findCustomerByUsername(username).isPresent();
    }

    @Override
    public boolean isExistedEmail(String email) {
        return repository.findCustomerByEmail(email).isPresent();
    }

    @Override
    public void delete(Customer customer) {
        Customer loadCustomer = findById(customer.getId()).orElseThrow(() ->
                new NotFoundException("Customer not found."));
        if (checkBeforeDelete(customer, loadCustomer, false))
            throw new CustomIllegalArgumentException("Customer email or username or password is incorrect.");

        super.delete(customer);
    }

    @Override
    public void deleteByAdmin(Customer customer) {
        Customer loadCustomer = findById(customer.getId()).orElseThrow(() ->
                new NotFoundException("Customer not found."));
        if (checkBeforeDelete(customer, loadCustomer, true))
            throw new CustomIllegalArgumentException("Customer email or username is incorrect.");

        super.delete(loadCustomer);
    }

    private boolean checkBeforeDelete(Customer customer, Customer loadCustomer, boolean deleteByAdmin) {
        if (customer.getUsername().equals(loadCustomer.getUsername()) &&
            customer.getEmail().equals(loadCustomer.getEmail()))
            return customer.getPassword().equals(loadCustomer.getPassword()) || deleteByAdmin;
        return false;
    }
}
