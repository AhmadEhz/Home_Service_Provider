package org.homeservice;

import org.homeservice.service.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
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
