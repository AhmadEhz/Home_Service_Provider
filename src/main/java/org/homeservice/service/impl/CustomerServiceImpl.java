package org.homeservice.service.impl;

import org.homeservice.entity.Customer;
import org.homeservice.repository.CustomerRepository;
import org.homeservice.service.CustomerService;
import org.homeservice.service.PersonService;
import org.homeservice.service.base.BaseServiceImpl;
import org.homeservice.util.Specifications;
import org.homeservice.util.exception.CustomIllegalArgumentException;
import org.homeservice.util.exception.NonUniqueException;
import org.homeservice.util.exception.NotFoundException;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Scope("singleton")
public class CustomerServiceImpl extends BaseServiceImpl<Customer, Long, CustomerRepository>
        implements CustomerService {
    private final PersonService personService;
    private final Specifications specifications;
    private final PasswordEncoder passwordEncoder;
    public CustomerServiceImpl(CustomerRepository repository, PersonService personService, Specifications specifications, PasswordEncoder passwordEncoder) {
        super(repository);
        this.personService = personService;
        this.specifications = specifications;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public void save(Customer customer) {
        validate(customer);
        if(isExistedUsername(customer.getUsername()))
            throw new NonUniqueException("Username is exist.");
        if(isExistedEmail(customer.getEmail()))
            throw new NonUniqueException("Email is exist.");
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        super.save(customer);
    }

    @Override
    public void changePassword(String username, String oldPassword, String newPassword) {
        Customer customer = repository.findCustomerByUsername(username).
                orElseThrow(() -> new NotFoundException("Customer not found."));
        if (!customer.getPassword().equals(oldPassword))
            throw new CustomIllegalArgumentException("Old password is incorrect.");
        customer.setPassword(passwordEncoder.encode(newPassword));
        update(customer);
    }

    @Override
    public List<Customer> loadAllByFilter(Map<String, String> filters) {
        return repository.findAll(specifications.getCustomer(filters));
    }

    @Override
    public boolean isExistedUsername(String username) {
        return personService.isExistsByUsername(username);
    }

    @Override
    @Transactional
    public void setVerificationId(Long id, Long verificationId) {
        repository.setVerificationCodeId(id, verificationId);
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
