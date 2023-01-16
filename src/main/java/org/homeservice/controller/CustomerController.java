package org.homeservice.controller;

import org.homeservice.dto.*;
import org.homeservice.entity.*;
import org.homeservice.service.*;
import org.homeservice.util.*;
import org.homeservice.util.exception.CustomIllegalArgumentException;
import org.homeservice.util.exception.NotFoundException;
import org.springframework.security.core.Authentication;
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
                              SubServiceService subServiceService, CreditService creditService,
                              VerifyCodeService verifyCodeService, EmailSender emailSender) {
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
        emailSender.send(customer.getEmail(), verifyCode, EmailSender.EmailFor.CUSTOMER);
        return "Signup success. please verify your email.";
    }

    @PutMapping("change-password")
    void changePassword(@RequestBody Map<String, String> map, Authentication user) {
        if (!map.containsKey("oldPassword") || !map.containsKey("newPassword"))
            throw new CustomIllegalArgumentException();
        customerService.changePassword(((Specialist) user.getPrincipal()).getUsername()
                , map.get("oldPassword"), map.get("newPassword"));
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
    void setOrder(@RequestBody OrderCreationDto orderCreationDto, Authentication user) {
        orderService.save(orderCreationDto.getOrder(), ((Customer) user.getPrincipal()).getId(),
                orderCreationDto.getSubServiceId());
    }

    @PostMapping("/send-rate")
    void saveRate(@RequestBody RateDto rateDto, Authentication user) {
        rateService.save(rateDto.getRate(), rateDto.getOrderId(), ((Customer) user.getPrincipal()).getId());
    }

    @GetMapping("/show-bids")
    List<BidDto> showBids(@RequestParam Long orderId, @RequestParam(required = false) String sort) {
        List<Bid> bids = bidService.loadAllByOrder(orderId, sort);
        return BidDto.convertToDto(bids);
    }

    @GetMapping("/orders/showAll")
    List<OrderDto> showOrders(@RequestParam Map<String, String> filters, Authentication user) {
        List<Order> orders = orderService.loadAllByFilterAndCustomer(filters, ((Customer) user.getPrincipal()).getId());
        return OrderDto.convertToDto(orders);
    }

    @GetMapping("/credit")
    CreditDto showCredit(Authentication user) {
        Credit credit = creditService.loadByCustomer(((Customer) user.getPrincipal()).getId())
                .orElseThrow(() -> new NotFoundException("Customer not found."));
        return new CreditDto(credit);
    }

    @PutMapping("/select-bid")
    void selectBid(@RequestParam Long bidId, Authentication user) {
        orderService.selectBid(bidId, ((Customer) user.getPrincipal()).getId());
    }

    @PutMapping("/start-work")
    void changeOrderStatusToStarted(@RequestParam Long orderId, Authentication user) {
        orderService.changeStatusToStarted(orderId, ((Customer) user.getPrincipal()).getId());
    }

    @PutMapping("/end-work")
    void changeOrderStatusToFinished(@RequestParam Long orderId, Authentication user) {
        orderService.changeStatusToEnded(orderId, ((Customer) user.getPrincipal()).getId());
    }

    @CrossOrigin
    @PostMapping("/payment")
    String payment(@RequestBody PaymentDto payment) {
        if (payment.getCaptcha() == null || payment.getCardNumber() == null)
            throw new CustomIllegalArgumentException("Necessary fields is empty.");
        CaptchaChecker captchaChecker = new CaptchaChecker(payment.getCaptcha());
        if (!captchaChecker.isValid())
            throw new CustomIllegalArgumentException("Captcha is invalid.");
        if ((int) Math.log10(payment.getCardNumber()) + 1 != Values.CARD_NUMBER_LENGTH)
            throw new CustomIllegalArgumentException("Card number is not " + Values.CARD_NUMBER_LENGTH + " digits.");
        return "Success";
    }

    @GetMapping("/verifyEmail")
    String verifyEmail(@RequestParam("verify") String verificationCode) {
        verifyCodeService.verifyCustomerEmail(verificationCode);
        return "Your email is verified.";
    }
}
