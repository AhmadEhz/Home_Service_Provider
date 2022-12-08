package org.homeservice.service.impl;

import org.homeservice.entity.Customer;
import org.homeservice.entity.Specialist;
import org.homeservice.repository.CustomerRepository;
import org.homeservice.repository.impl.CustomerRepositoryImpl;
import org.homeservice.service.CustomerService;
import org.homeservice.service.SpecialistService;
import org.homeservice.service.base.BaseServiceImpl;

import java.util.List;

public class CustomerServiceImpl extends BaseServiceImpl<Customer,Long, CustomerRepository>
        implements CustomerService {
    private static CustomerService service;
    private final SpecialistService specialistService;
    private CustomerServiceImpl() {
        super(CustomerRepositoryImpl.getRepository());
        specialistService = SpecialistServiceImpl.getService();
    }

    public List<Specialist> findAllVerifiedSpecialist() {
        return specialistService.loadVerifiedSpecialists();
    }
    public static CustomerService getService() {
        if(service == null)
            service = new CustomerServiceImpl();
        return service;
    }
}
