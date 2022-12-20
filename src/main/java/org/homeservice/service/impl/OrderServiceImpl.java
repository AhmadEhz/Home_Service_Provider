package org.homeservice.service.impl;

import org.homeservice.entity.Bid;
import org.homeservice.entity.Customer;
import org.homeservice.entity.Order;
import org.homeservice.entity.OrderStatus;
import org.homeservice.repository.OrderRepository;
import org.homeservice.service.BidService;
import org.homeservice.service.CustomerService;
import org.homeservice.service.OrderService;
import org.homeservice.service.base.BaseServiceImpl;
import org.homeservice.util.QueryUtil;
import org.homeservice.util.exception.CustomIllegalArgumentException;
import org.homeservice.util.exception.NotFoundException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Scope("singleton")
public class OrderServiceImpl extends BaseServiceImpl<Order, Long, OrderRepository> implements OrderService {

    private final BidService bidService;
    private final CustomerService customerService;

    public OrderServiceImpl(OrderRepository repository, BidService bidService, CustomerService customerService) {
        super(repository);
        this.bidService = bidService;
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
        Bid bid = bidService.findById(bidId).orElseThrow(() -> new NotFoundException("Bid not found."));
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
    public void changeStatusToChooseSpecialist(Long id) {
        Order order = findById(id).orElseThrow(() -> new NotFoundException("Order not found."));
        if (!order.checkStatusIfWaitingForBids()) {
            throw new CustomIllegalArgumentException
                    ("Can't change order status to \"Waiting for Specialist\". Customer accepted a bid.");
        }
        repository.changeStatus(id, OrderStatus.WAITING_FOR_CHOOSE_SPECIALIST);
    }
}
