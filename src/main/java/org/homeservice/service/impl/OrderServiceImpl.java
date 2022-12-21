package org.homeservice.service.impl;

import org.homeservice.entity.*;
import org.homeservice.repository.OrderRepository;
import org.homeservice.service.*;
import org.homeservice.service.base.BaseServiceImpl;
import org.homeservice.util.exception.CustomIllegalArgumentException;
import org.homeservice.util.exception.NotFoundException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Scope("singleton")
@ComponentScan(basePackages = "org.homeservice.service.*")
public class OrderServiceImpl extends BaseServiceImpl<Order, Long, OrderRepository> implements OrderService {

    private final ApplicationContext applicationContext;
    private final CustomerService customerService;
    private BidService bidService;

    public OrderServiceImpl(OrderRepository repository, ApplicationContext applicationContext, CustomerService customerService) {
        super(repository);
        this.applicationContext = applicationContext;
        this.customerService = customerService;
    }

    @Override
    public List<Order> findAllByCustomer(Long customerId) {
        return repository.findAllByCustomer_Id(customerId);
    }

    @Override
    public List<Order> findAllBySpecialist(Long specialistId) {
        return repository.findAllBySpecialist_Id(specialistId);
    }

    @Override
    public void selectBidForOffer(Long bidId, Long customerId) {
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
        Bid bid;
        Customer customer = customerService.findById(customerId).orElseThrow
                (() -> new NotFoundException("Customer not found."));

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

        repository.changeStatus(id, OrderStatus.STARTED);
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
