package org.homeservice.controller;

import org.homeservice.dto.BidCreationDto;
import org.homeservice.dto.OrderDto;
import org.homeservice.dto.SpecialistCreationDto;
import org.homeservice.entity.Order;
import org.homeservice.entity.Specialist;
import org.homeservice.service.BidService;
import org.homeservice.service.OrderService;
import org.homeservice.service.SpecialistService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/specialist")
public class SpecialistController {
    private final SpecialistService specialistService;
    private final BidService bidService;
    private final OrderService orderService;

    public SpecialistController(SpecialistService specialistService, BidService bidService, OrderService orderService) {
        this.specialistService = specialistService;
        this.bidService = bidService;
        this.orderService = orderService;
    }

    @GetMapping("/order/showAll")
    List<OrderDto> showOrders(@RequestParam("id") Long specialistId) {
        List<Order> orders = orderService.loadAllBySpecialistSubServices(specialistId);
        return OrderDto.convertToDto(orders);
    }

    @GetMapping("/order/showAll-waiting-for-bid")
    List<OrderDto> showOrdersByWaitingStatus(@RequestParam("id") Long specialistId) {
        List<Order> orders = orderService.loadAllByWaitingStatusAndSpecialist(specialistId);
        return OrderDto.convertToDto(orders);
    }

    @PostMapping("/save")
    void save(@RequestBody SpecialistCreationDto specialistDto) {
        specialistService.save(specialistDto.getSpecialist());
    }

    @PutMapping("/change-password")
    void changePassword(Map<String, String> map) {
        specialistService.changePassword(map.get("username"), map.get("oldPassword"), map.get("newPassword"));
    }

    @PutMapping("/add-avatar/{id}")
    void addAvatar(@PathVariable Long id, @RequestBody MultipartFile avatar) {
        specialistService.addAvatar(id, avatar);
    }

    @PutMapping ("/set-bid")
    void saveBid(@RequestBody BidCreationDto bidCreationDTO) {
        bidService.save(bidCreationDTO.getBid(), bidCreationDTO.getOrderId(), bidCreationDTO.getSpecialistId());
    }
}
