package org.homeservice.controller;

import org.homeservice.dto.OrderDto;
import org.homeservice.entity.Customer;
import org.homeservice.service.CustomerService;
import org.homeservice.service.OrderService;
import org.homeservice.service.RateService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    private final CustomerService customerService;
    private final OrderService orderService;
    private final RateService rateService;

    public CustomerController(CustomerService customerService, OrderService orderService, RateService rateService) {
        this.customerService = customerService;
        this.orderService = orderService;
        this.rateService = rateService;
    }

    @PostMapping("/save")
    void save(@RequestBody Customer customer) {
        customerService.save(customer);
    }

    @PutMapping("change-password")
    void changePassword(@RequestBody Map<String, String> map) {
        customerService.changePassword(map.get("username"), map.get("oldPassword"), map.get("newPassword"));
    }

    @PostMapping("send-order")
    void setOrder(@RequestBody OrderDto orderDto) {
        orderService.save(orderDto.getOrder(), orderDto.getCustomerId(), orderDto.getSubServiceId());
    }
}
