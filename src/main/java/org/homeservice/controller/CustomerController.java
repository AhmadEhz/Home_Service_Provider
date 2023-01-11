package org.homeservice.controller;

import org.homeservice.dto.*;
import org.homeservice.entity.*;
import org.homeservice.service.*;
import org.homeservice.util.CaptchaChecker;
import org.homeservice.util.EmailSender;
import org.homeservice.util.Values;
import org.homeservice.util.exception.CustomIllegalArgumentException;
import org.homeservice.util.exception.NotFoundException;
import org.springframework.transaction.annotation.Transactional;
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
    private final CreditService creditService;
    private final VerifyCodeService verifyCodeService;
    private final EmailSender emailSender;


    public CustomerController(CustomerService customerService, OrderService orderService,
                              RateService rateService, BidService bidService, ServiceService serviceService,
                              SubServiceService subServiceService, CreditService creditService, VerifyCodeService verifyCodeService, EmailSender emailSender) {
        this.customerService = customerService;
        this.orderService = orderService;
        this.rateService = rateService;
        this.bidService = bidService;
        this.serviceService = serviceService;
        this.subServiceService = subServiceService;
        this.creditService = creditService;
        this.verifyCodeService = verifyCodeService;
        this.emailSender = emailSender;
    }

    @PostMapping("/save")
    @Transactional
    String save(@RequestBody CustomerCreationDto customerDto) {
        Customer customer = customerDto.getCustomer();
        customerService.save(customer);
        String verifyCode = verifyCodeService.generateAndSaveForCustomer(customer.getId());
        emailSender.sendSimpleMessage(customer.getEmail(), verifyCode, EmailSender.EmailFor.CUSTOMER);
        return "Signup success. please verify your email.";
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

    @PostMapping("/send-rate")
    void saveRate(@RequestBody RateDto rateDto) {
        rateService.save(rateDto.getRate(), rateDto.getOrderId(), rateDto.getCustomerId());
    }

    @GetMapping("/show-bids")
    List<BidDto> showBids(@RequestParam Long orderId, @RequestParam(required = false) String sort) {
        List<Bid> bids = bidService.loadAllByOrder(orderId, sort);
        return BidDto.convertToDto(bids);
    }

    @GetMapping("/credit")
    CreditDto showCredit(@RequestParam Long customerId) {
        Credit credit = creditService.loadByCustomer(customerId)
                .orElseThrow(() -> new NotFoundException("Customer not found."));
        return new CreditDto(credit);
    }

    @CrossOrigin
    @PostMapping("/payment")
    String payment(@RequestBody PaymentDto payment) {
        CaptchaChecker captchaChecker = new CaptchaChecker(payment.getCaptcha());
        if (!captchaChecker.isValid())
            throw new CustomIllegalArgumentException("Captcha is invalid.");
        if ((int) Math.log10(payment.getCardNumber()) + 1 != Values.CARD_NUMBER_LENGTH)
            throw new CustomIllegalArgumentException("Card number is not " + Values.CARD_NUMBER_LENGTH + " digits.");
        return "true";
    }

    @PutMapping("/select-bid")
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

    @GetMapping("/verifyEmail")
    String verifyEmail(@RequestParam("verify") String verificationCode) {
        verifyCodeService.verifyCustomerEmail(verificationCode);
        return "Your email is verified.";
    }
}
