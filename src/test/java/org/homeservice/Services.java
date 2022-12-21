package org.homeservice;

import org.homeservice.service.*;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@SpringBootTest
@Component
@Scope("singleton")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Services {
     AdminService adminService;
     CustomerService customerService;
     SpecialistService specialistService;
     OrderService orderService;
     BidService bidService;
     RateService rateService;
     ServiceService serviceService;
     SubServiceService subServiceService;

    public Services( AdminService adminService, CustomerService customerService,
                     SpecialistService specialistService, OrderService orderService,
                     BidService bidService, RateService rateService, ServiceService serviceService,
                     SubServiceService subServiceService) {
        this.adminService = adminService;
        this.customerService = customerService;
        this.specialistService = specialistService;
        this.orderService = orderService;
        this.bidService = bidService;
        this.rateService = rateService;
        this.serviceService = serviceService;
        this.subServiceService = subServiceService;
    }

}
