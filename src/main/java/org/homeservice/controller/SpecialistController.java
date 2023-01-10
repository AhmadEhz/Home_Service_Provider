package org.homeservice.controller;

import org.homeservice.dto.BidCreationDto;
import org.homeservice.dto.CreditDto;
import org.homeservice.dto.OrderDto;
import org.homeservice.dto.SpecialistCreationDto;
import org.homeservice.entity.Credit;
import org.homeservice.entity.Order;
import org.homeservice.entity.Specialist;
import org.homeservice.service.*;
import org.homeservice.util.EmailSender;
import org.homeservice.util.exception.NotFoundException;
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
    private final CreditService creditService;
    private final VerifyCodeService verifyCodeService;
    private final EmailSender emailSender;

    public SpecialistController(SpecialistService specialistService, BidService bidService, OrderService orderService,
                                CreditService creditService, VerifyCodeService verifyCodeService,
                                EmailSender emailSender) {
        this.specialistService = specialistService;
        this.bidService = bidService;
        this.orderService = orderService;
        this.creditService = creditService;
        this.verifyCodeService = verifyCodeService;
        this.emailSender = emailSender;
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

    @GetMapping("/credit")
    CreditDto showCredit(@RequestParam Long specialistId) {
        Credit credit = creditService.loadBySpecialist(specialistId)
                .orElseThrow(() -> new NotFoundException("Specialist not found."));
        return new CreditDto(credit);
    }

    @PostMapping("/save")
    void save(@RequestBody SpecialistCreationDto specialistDto) {
        Specialist specialist = specialistDto.getSpecialist();
        specialistService.save(specialist);
        String generatedCode = verifyCodeService.generateAndSaveForSpecialist(specialist.getId());
        emailSender.sendVerifyingEmail(specialist.getEmail(), generatedCode, EmailSender.EmailFor.SPECIALIST);
    }

    @PutMapping("/change-password")
    void changePassword(Map<String, String> map) {
        specialistService.changePassword(map.get("username"), map.get("oldPassword"), map.get("newPassword"));
    }

    @PutMapping("/add-avatar/")
    void addAvatar(@RequestParam Long id, @RequestBody MultipartFile avatar) {
        specialistService.addAvatar(id, avatar);
    }

    @PostMapping("/set-bid")
    void saveBid(@RequestBody BidCreationDto bidCreationDto) {
        bidService.save(bidCreationDto.getBid(), bidCreationDto.getOrderId(), bidCreationDto.getSpecialistId());
    }

    @GetMapping("/verifyEmail")
    String verifyEmail(@RequestParam("verify") String verificationCode) {
        verifyCodeService.verifySpecialistEmail(verificationCode);
        return "Your email is verified.";
    }
}
