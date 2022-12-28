package org.homeservice.controller;

import org.homeservice.dto.*;
import org.homeservice.entity.Bid;
import org.homeservice.entity.Service;
import org.homeservice.entity.SubService;
import org.homeservice.service.*;
import org.homeservice.util.exception.NotFoundException;
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
    private final ServiceService serviceService;
    private final SubServiceService subServiceService;

    public CustomerController(CustomerService customerService, OrderService orderService,
                              RateService rateService, BidService bidService, ServiceService serviceService,
                              SubServiceService subServiceService) {
        this.customerService = customerService;
        this.orderService = orderService;
        this.rateService = rateService;
        this.bidService = bidService;
        this.serviceService = serviceService;
        this.subServiceService = subServiceService;
    }

    @PostMapping("/save")
    void save(@RequestBody CustomerCreationDto customerDto) {
        customerService.save(customerDto.getCustomer());
    }

    @PutMapping("change-password")
    void changePassword(@RequestBody Map<String, String> map) {
        customerService.changePassword(map.get("username"), map.get("oldPassword"), map.get("newPassword"));
    }

    @GetMapping("/show-service")
    ServiceDto showService(@RequestParam Long id) {
        Service service = serviceService.findById(id).orElseThrow(() -> new NotFoundException("Service not found."));
        return new ServiceDto(service);
    }

    @GetMapping("/show-subService")
    SubServiceDto showSubService(@RequestParam Long id) {
        SubService subService = subServiceService.findById(id).orElseThrow(() ->
                new NotFoundException("SubService not found."));
        return new SubServiceDto(subService);
    }

    @GetMapping("/show-subServices")
    List<SubServiceDto> showSubServices(@RequestParam Long serviceId) {
        List<SubService> subServices = subServiceService.loadAllByService(serviceId);
        return SubServiceDto.convertToDto(subServices);
    }

    @GetMapping("/show-services")
    List<ServiceDto> showServices() {
        List<Service> services = serviceService.findAll();
        return ServiceDto.convertToDto(services);
    }

    @PostMapping("/set-order")
    void setOrder(@RequestBody OrderCreationDto orderCreationDto) {
        orderService.save(orderCreationDto.getOrder(), orderCreationDto.getCustomerId(),
                orderCreationDto.getSubServiceId());
    }

    @PostMapping("/set-rate")
    void setRate(@RequestBody RateDto rateDto) {
        rateService.save(rateDto.getRate(), rateDto.getOrderId(), rateDto.getCustomerId());
    }

    @GetMapping("/show-bids")
    List<BidDto> showBids(@RequestParam Long orderId, @RequestParam(required = false) String sort) {
        List<Bid> bids = bidService.loadAllByOrder(orderId, sort);
        return BidDto.convertToDto(bids);
    }

    @PutMapping("select-bid")
    void selectBid(@RequestParam Long bidId, @RequestParam Long customerId) {
        orderService.selectBid(bidId, customerId);
    }

    @PutMapping("/start-work")
    void changeOrderStatusToStarted(@RequestParam Long orderId, @RequestParam Long customerId) {
        orderService.changeStatusToStarted(orderId, customerId);
    }

    @PutMapping("/end-work")
    void changeOrderStatusToFinished(@RequestParam Long orderId, @RequestParam Long customerId) {
        orderService.changeStatusToEnded(orderId, customerId);
    }

    @PostMapping("/send-rate")
    void saveRate(@RequestBody RateDto rateDto) {
        rateService.save(rateDto.getRate(), rateDto.getOrderId(), rateDto.getCustomerId());
    }
}
