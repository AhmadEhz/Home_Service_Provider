package org.homeservice.service.impl;

import org.homeservice.entity.Customer;
import org.homeservice.repository.CustomerRepository;
import org.homeservice.service.CustomerService;
import org.homeservice.service.base.BaseServiceImpl;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
public class CustomerServiceImpl extends BaseServiceImpl<Customer,Long, CustomerRepository>
        implements CustomerService {
    public CustomerServiceImpl(CustomerRepository repository) {
        super(repository);
    }
}
