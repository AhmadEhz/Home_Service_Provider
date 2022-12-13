package org.homeservice.service;

import org.homeservice.entity.Customer;
import org.homeservice.service.base.BaseService;
import org.springframework.stereotype.Service;

@Service
public interface CustomerService extends BaseService<Customer,Long> {
}
