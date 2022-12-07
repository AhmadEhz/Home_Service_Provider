package org.homeservice.service.impl;

import org.homeservice.entity.Customer;
import org.homeservice.repository.CustomerRepository;
import org.homeservice.repository.impl.CustomerRepositoryImpl;
import org.homeservice.service.CustomerService;
import org.homeservice.service.base.BaseServiceImpl;

public class CustomerServiceImpl extends BaseServiceImpl<Customer,Long, CustomerRepository>
        implements CustomerService {
    private static CustomerService service;
    private CustomerServiceImpl() {
        super(CustomerRepositoryImpl.getRepository());
    }

    public static CustomerService getService() {
        if(service == null)
            service = new CustomerServiceImpl();
        return service;
    }
}
