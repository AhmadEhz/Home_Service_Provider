package org.homeservice;

import org.homeservice.entity.*;
import org.homeservice.service.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class Services {
    final AdminService adminService;
    final CustomerService customerService;
    final SpecialistService specialistService;
    final OrderService orderService;
    final BidService bidService;
    final RateService rateService;
    final ServiceService serviceService;
    final SubServiceService subServiceService;
    final SubServiceSpecialistService subServiceSpecialistService;

    public Services(AdminService adminService, CustomerService customerService,
                    SpecialistService specialistService, OrderService orderService,
                    BidService bidService, RateService rateService, ServiceService serviceService,
                    SubServiceService subServiceService, SubServiceSpecialistService subServiceSpecialistService) {
        this.adminService = adminService;
        this.customerService = customerService;
        this.specialistService = specialistService;
        this.orderService = orderService;
        this.bidService = bidService;
        this.rateService = rateService;
        this.serviceService = serviceService;
        this.subServiceService = subServiceService;
        this.subServiceSpecialistService = subServiceSpecialistService;
    }

    Order loadOrder(Long orderId) {
        return orderService.loadById(orderId).get();
    }
    Bid loadBid(Long bidId) {
        return bidService.loadById(bidId).get();
    }
    Specialist loadSpecialist(Long specialistId) {
        return specialistService.loadById(specialistId).get();
    }
    Customer loadCustomer(Long customerId) {
        return customerService.loadById(customerId).get();
    }
    Admin loadAdmin(Long adminId) {
        return adminService.loadById(adminId).get();
    }
    Service loadService(Long serviceId) {
        return serviceService.loadById(serviceId).get();
    }
    SubService loadSubService(Long subServiceId) {
        return subServiceService.loadById(subServiceId).get();
    }
}
