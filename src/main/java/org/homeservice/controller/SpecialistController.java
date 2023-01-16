package org.homeservice.controller;

import org.homeservice.dto.*;
import org.homeservice.entity.*;
import org.homeservice.service.*;
import org.homeservice.util.EmailSender;
import org.homeservice.util.exception.CustomIllegalArgumentException;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
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
    private final VerifyCodeService verifyCodeService;
    private final EmailSender emailSender;

    public SpecialistController(SpecialistService specialistService, BidService bidService, OrderService orderService,
                                VerifyCodeService verifyCodeService, EmailSender emailSender) {
        this.specialistService = specialistService;
        this.bidService = bidService;
        this.orderService = orderService;
        this.verifyCodeService = verifyCodeService;
        this.emailSender = emailSender;
    }

    @PostMapping("/save")
    @Transactional
    String save(@RequestBody SpecialistCreationDto specialistDto) {
        Specialist specialist = specialistDto.getSpecialist();
        specialistService.save(specialist);
        String generatedCode = verifyCodeService.generateAndSaveForSpecialist(specialist.getId());
        emailSender.send(specialist.getEmail(), generatedCode, EmailSender.EmailFor.SPECIALIST);
        return "Signup success. please verify your email.";
    }

    @PutMapping("/change-password")
    void changePassword(Map<String, String> map, Authentication user) {
        if (!map.containsKey("oldPassword") || !map.containsKey("newPassword"))
            throw new CustomIllegalArgumentException();
        specialistService.changePassword((Specialist) user.getPrincipal(),
                map.get("oldPassword"), map.get("newPassword"));
    }

    @GetMapping("/order/showAll-related")
    List<OrderDto> showOrders(Authentication user) {
        List<Order> orders = orderService.loadAllBySpecialistSubServices(((Specialist) user.getPrincipal()).getId());
        return OrderDto.convertToDto(orders);
    }

    @GetMapping("/order/showAll")
    List<OrderDto> showDoneOrders(@RequestParam Map<String, String> filters, Authentication user) {
        List<Order> orders = orderService.loadAllByFilterAndCustomer(filters, (
                (Specialist) user.getPrincipal()).getId());
        return OrderDto.convertToDto(orders);
    }

    @GetMapping("/order/showAll-waiting-for-bid")
    List<OrderDto> showOrdersByWaitingStatus(Authentication user) {
        List<Order> orders = orderService.loadAllByWaitingStatusAndSpecialist(
                ((Specialist) user.getPrincipal()).getId());
        return OrderDto.convertToDto(orders);
    }

    @GetMapping("/credit")
    CreditDto showCredit(Authentication user) {
        Credit credit = ((Specialist) user.getPrincipal()).getCredit();
        return new CreditDto(credit);
    }

    @PutMapping("/add-avatar/")
    void addAvatar(@RequestBody MultipartFile avatar, Authentication user) {
        specialistService.addAvatar((Specialist) user.getPrincipal(), avatar);
    }

    @PostMapping("/set-bid")
    void saveBid(@RequestBody BidCreationDto bidCreationDto, Authentication user) {
        bidService.save(bidCreationDto.getBid(), bidCreationDto.getOrderId(),
                (Specialist) user.getPrincipal());
    }

    @GetMapping("/verifyEmail")
    String verifyEmail(@RequestParam("verify") String verificationCode) {
        verifyCodeService.verifySpecialistEmail(verificationCode);
        return "Your email is verified.";
    }
}
