package org.homeservice.controller;

import org.homeservice.dto.CreationBidDto;
import org.homeservice.dto.OrderDto;
import org.homeservice.dto.RateDto;
import org.homeservice.entity.Bid;
import org.homeservice.entity.Customer;
import org.homeservice.service.BidService;
import org.homeservice.service.CustomerService;
import org.homeservice.service.OrderService;
import org.homeservice.service.RateService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    void setOrder(@RequestBody OrderDto orderDto) {
        orderService.save(orderDto.getOrder(), orderDto.getCustomerId(), orderDto.getSubServiceId());
    }

    @PostMapping("set-rate")
    void setRate(@RequestBody RateDto rateDto) {
        rateService.save(rateDto.getRate(), rateDto.getOrderId(), rateDto.getCustomerId());
    }

    @GetMapping("show-bids")
    List<CreationBidDto> showBids(@RequestParam Long orderId, @RequestParam String sort) {
        List<Bid> bids = null;
        if (sort.equalsIgnoreCase("price"))
            bids = bidService.loadAllByOrderSortedByPrice(orderId);
        if(sort.equalsIgnoreCase("specialist"))
            bids = bidService.loadAllByOrderSortedBySpecialistScore(orderId);
        if(bids.isEmpty()) return null;
        List<CreationBidDto> creationBidDtoList = new ArrayList<>(bids.size());
        for (Bid b : bids) {
            creationBidDtoList.add(new CreationBidDto(b));
        }
        return creationBidDtoList;
    }
}
