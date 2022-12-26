package org.homeservice.service.impl;

import org.homeservice.entity.*;
import org.homeservice.repository.OrderRepository;
import org.homeservice.service.*;
import org.homeservice.service.base.BaseServiceImpl;
import org.homeservice.util.exception.CustomIllegalArgumentException;
import org.homeservice.util.exception.NotFoundException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Scope("singleton")
public class OrderServiceImpl extends BaseServiceImpl<Order, Long, OrderRepository> implements OrderService {

    private final ApplicationContext applicationContext;
    private final CustomerService customerService;
    private final SubServiceService subServiceService;
    private BidService bidService;

    public OrderServiceImpl(ApplicationContext applicationContext, OrderRepository repository,
                            CustomerService customerService, SubServiceService subServiceService) {
        super(repository);
        this.applicationContext = applicationContext;
        this.customerService = customerService;
        this.subServiceService = subServiceService;
    }

    @Override
    public void save(Order order, Long customerId, Long subServiceId) {
        Customer customer = customerService.findById(customerId).orElseThrow(
                () -> new NotFoundException("Customer not found."));
        SubService subService = subServiceService.findById(subServiceId).orElseThrow(
                () -> new NotFoundException("SubService not found."));

        order.setCustomer(customer);
        order.setSubService(subService);
        save(order);
    }

    @Override
    public void save(Order order) {
        if (order.getSubService() == null)
            throw new NullPointerException("SubService is null.");
        if (order.getCustomer() == null)
            throw new NullPointerException("Customer is null");

        if (order.getCustomerOfferPrice() < order.getSubService().getBasePrice())
            throw new CustomIllegalArgumentException("Offer price should not be less than SubService base price.");

        super.save(order);
    }

    @Override
    public List<Order> loadAllByCustomer(Long customerId) {
        return repository.findAllByCustomer_Id(customerId);
    }

    @Override
    public List<Order> loadAllBySpecialist(Long specialistId) {
        return repository.findAllBySpecialist_Id(specialistId);
    }

    @Override
    public List<Order> loadAllBySpecialistSubServices(Long specialistId) {
        return repository.findBySpecialistSubServices(specialistId);
    }

    @Override
    public List<Order> loadAllByWaitingStatusAndSpecialist(Long specialistId) {
        return repository.findAllBySpecialistAndStatus(specialistId,
                new OrderStatus[]{OrderStatus.WAITING_FOR_CHOOSE_SPECIALIST, OrderStatus.WAITING_FOR_BID});
    }

    @Override
    public void selectBid(Long bidId, Long customerId) {
        Bid bid = getBidService().findById(bidId).orElseThrow(() -> new NotFoundException("Bid not found."));
        Customer customer = customerService.findById(customerId).orElseThrow(
                () -> new NotFoundException("Customer not found."));
        Order order = bid.getOrder();

        if (!order.getCustomer().equals(customer))
            throw new CustomIllegalArgumentException("This Order is not for this Customer");
        if (!order.checkStatusIfWaitingForBids())
            throw new CustomIllegalArgumentException("This order accepted a bid before this.");

        order.setFinalPrice(bid.getOfferPrice());
        order.setSpecialist(bid.getSpecialist());
        order.setStatus(OrderStatus.WAITING_FOR_COMING_SPECIALIST);
        update(order);
    }

    @Override
    @Transactional
    public void changeStatusToChooseSpecialist(Long id, Long customerId) {
        Order order = findById(id).orElseThrow(() -> new NotFoundException("Order not found."));
        Customer customer = customerService.findById(customerId).
                orElseThrow(() -> new NotFoundException("Customer not found."));
        if (!order.getCustomer().equals(customer))
            throw new CustomIllegalArgumentException("This Order is not for this Customer.");
        if (!order.checkStatusIfWaitingForBids()) {
            throw new CustomIllegalArgumentException
                    ("Can't change order status to \"Waiting for Specialist\". Customer accepted a bid.");
        }
        repository.changeStatus(id, OrderStatus.WAITING_FOR_CHOOSE_SPECIALIST);
    }

    @Override
    @Transactional
    public void changeStatusToStarted(Long id, Long customerId) {
        Order order = findById(id).orElseThrow(() -> new NotFoundException("Order not found."));
        Customer customer = customerService.findById(customerId).orElseThrow
                (() -> new NotFoundException("Customer not found."));
        Bid bid;

        if (!order.getCustomer().equals(customer))
            throw new CustomIllegalArgumentException("This Order is not for this Customer.");
        if (order.getStatus() != OrderStatus.WAITING_FOR_COMING_SPECIALIST)
            throw new CustomIllegalArgumentException
                    ("The status of Order is not in \"Waiting for coming specialist\"");
        if (!isAcceptedBid(id))
            throw new CustomIllegalArgumentException("This Order not yet accepted a Bid.");

        bid = getBidService().loadByCustomerAndSpecialist(customerId, order.getSpecialist().getId())
                .orElseThrow(() -> new NotFoundException("The Bid for this Order not found."));
        if (LocalDateTime.now().isBefore(bid.getStartWorking()))
            throw new CustomIllegalArgumentException("Starting the work should be after Bid start time");
        order.setStatus(OrderStatus.STARTED);
        order.setStartWorkingTime(LocalDateTime.now());
        super.update(order);
    }

    @Override
    public boolean isAcceptedBid(Long id) {
        return repository.findIfAcceptedABid(id).isPresent();
    }

    private BidService getBidService() {
        if (bidService == null)
            bidService = applicationContext.getBean(BidService.class);
        return bidService;
    }
}
