package org.homeservice.controller;

import org.homeservice.service.CustomerService;
import org.homeservice.service.OrderService;
import org.homeservice.service.RateService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {
    private final CustomerService customerService;
    private final OrderService orderService;
    private final RateService rateService;

    public CustomerController(CustomerService customerService, OrderService orderService, RateService rateService) {
        this.customerService = customerService;
        this.orderService = orderService;
        this.rateService = rateService;
    }
}
