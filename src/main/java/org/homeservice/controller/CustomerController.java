package org.homeservice.controller;

import org.homeservice.dto.BidDto;
import org.homeservice.dto.OrderCreationDto;
import org.homeservice.dto.RateDto;
import org.homeservice.entity.Bid;
import org.homeservice.entity.Customer;
import org.homeservice.service.BidService;
import org.homeservice.service.CustomerService;
import org.homeservice.service.OrderService;
import org.homeservice.service.RateService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    private final CustomerService customerService;
    private final OrderService orderService;
    private final RateService rateService;
    private final BidService bidService;

    public CustomerController(CustomerService customerService, OrderService orderService,
                              RateService rateService, BidService bidService) {
        this.customerService = customerService;
        this.orderService = orderService;
        this.rateService = rateService;
        this.bidService = bidService;
    }

    @PostMapping("/save")
    void save(@RequestBody Customer customer) {
        customerService.save(customer);
    }

    @PutMapping("change-password")
    void changePassword(@RequestBody Map<String, String> map) {
        customerService.changePassword(map.get("username"), map.get("oldPassword"), map.get("newPassword"));
    }

    @PostMapping("set-order")
    void setOrder(@RequestBody OrderCreationDto orderCreationDto) {
        orderService.save(orderCreationDto.getOrder(), orderCreationDto.getCustomerId(),
                orderCreationDto.getSubServiceId());
    }

    @PostMapping("set-rate")
    void setRate(@RequestBody RateDto rateDto) {
        rateService.save(rateDto.getRate(), rateDto.getOrderId(), rateDto.getCustomerId());
    }

    @GetMapping("/show-bids")
    List<BidDto> showBids(@RequestParam Long orderId, @RequestParam String sort) {
        List<Bid> bids = bidService.loadAllByOrder(orderId, sort);
        return BidDto.convertToDto(bids);
    }

    @PutMapping("/start-order")
    void changeOrderStatusToStarted(@RequestParam Long orderId, @RequestParam Long customerId) {
        orderService.changeStatusToStarted(orderId, customerId);
    }
}
