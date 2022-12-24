package org.homeservice.controller;

import org.homeservice.service.CustomerService;
import org.homeservice.service.OrderService;
import org.homeservice.service.RateService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {
    private CustomerService customerService;
    private OrderService orderService;
    private RateService rateService;
}
