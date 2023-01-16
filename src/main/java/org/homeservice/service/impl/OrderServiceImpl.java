package org.homeservice.service.impl;

import org.homeservice.entity.*;
import org.homeservice.repository.OrderRepository;
import org.homeservice.service.*;
import org.homeservice.service.base.BaseServiceImpl;
import org.homeservice.util.Specifications;
import org.homeservice.util.exception.CustomIllegalArgumentException;
import org.homeservice.util.exception.NotFoundException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Scope("singleton")
public class OrderServiceImpl extends BaseServiceImpl<Order, Long, OrderRepository> implements OrderService {

    private final ApplicationContext applicationContext;
    private final SubServiceService subServiceService;
    private final Specifications specifications;
    private BidService bidService;

    public OrderServiceImpl(ApplicationContext applicationContext, OrderRepository repository,
                            SubServiceService subServiceService, Specifications specifications) {
        super(repository);
        this.applicationContext = applicationContext;
        this.subServiceService = subServiceService;
        this.specifications = specifications;
    }

    @Override
    public void save(Order order, Customer customer, Long subServiceId) {
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
                OrderStatus.WAITING_FOR_CHOOSE_SPECIALIST, OrderStatus.WAITING_FOR_BID);
    }

    @Override
    public List<Order> loadAllByFilterAndCustomer(Map<String, String> filters, Long customerId) {
        // Put CustomerId to filters to get Orders for this customer only.
        filters.put("customerId", String.valueOf(customerId));
        return repository.findAllWithDetails(filters);
    }

    @Override
    public List<Order> loadAllByFilterAndSpecialist(Map<String, String> filters, Long specialistId) {
        filters.put("specialistId", String.valueOf(specialistId));
        return repository.findAll(specifications.getOrder(filters));
    }

    @Override
    public List<Order> loadAllWithDetails(Map<String, String> filters) {
        return repository.findAllWithDetails(filters);
    }

    @Override
    public void selectBid(Long bidId, Customer customer) {
        Bid bid = getBidService().findById(bidId).orElseThrow(() -> new NotFoundException("Bid not found."));
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
    public void changeStatusToChooseSpecialist(Order order, Customer customer) {
        if (!order.getCustomer().equals(customer))
            throw new CustomIllegalArgumentException("This Order is not for this Customer.");
        if (!order.checkStatusIfWaitingForBids()) {
            throw new CustomIllegalArgumentException
                    ("Can't change order status to \"Waiting for Specialist\". Customer accepted a bid.");
        }
        repository.changeStatus(order.getId(), OrderStatus.WAITING_FOR_CHOOSE_SPECIALIST);
    }

    @Override
    @Transactional
    public void changeStatusToStarted(Long id, Customer customer) {
        Order order = findById(id).orElseThrow(() -> new NotFoundException("Order not found."));
        Bid bid;

        if (!order.getCustomer().equals(customer))
            throw new CustomIllegalArgumentException("This Order is not for this Customer.");
        if (order.getStatus() != OrderStatus.WAITING_FOR_COMING_SPECIALIST)
            throw new CustomIllegalArgumentException
                    ("The status of Order is not in \"Waiting for coming specialist\"");
        if (!isAcceptedBid(id))
            throw new CustomIllegalArgumentException("This Order not yet accepted a Bid.");

        bid = getBidService().loadByCustomerAndSpecialist(customer.getId(), order.getSpecialist().getId())
                .orElseThrow(() -> new NotFoundException("The Bid for this Order not found."));
        if (LocalDateTime.now().isBefore(bid.getStartWorking()))
            throw new CustomIllegalArgumentException("Starting the work should be after Bid start time");
        order.setStatus(OrderStatus.STARTED);
        order.setStartWorking(LocalDateTime.now());
        super.update(order);
    }

    @Override
    @Transactional
    public void changeStatusToEnded(Long id, Customer customer) {
        Order order = findById(id).orElseThrow(() -> new NotFoundException("Order not found."));
        Bid bid;
        if (!order.getCustomer().equals(customer))
            throw new CustomIllegalArgumentException("This Order is not for this Customer.");
        if (order.getStatus() != OrderStatus.STARTED)
            throw new CustomIllegalArgumentException
                    ("The status of Order is not in \"Started\"");
        bid = getBidService().loadByCustomerAndSpecialist(customer.getId(), order.getSpecialist().getId())
                .orElseThrow(() -> new NotFoundException("The Bid for this Order not found."));
        Duration lateness = Duration.between(bid.getEndWorking(), LocalDateTime.now());
        order.setLatenessEndWorking(lateness.getSeconds() > 0 ? lateness : Duration.ZERO);
        order.setStatus(OrderStatus.FINISHED);
        order.setEndWorking(LocalDateTime.now());
        super.update(order);
    }

    @Override
    @Transactional
    public void setRateId(Long orderId, Long rateId) {
        repository.setRateId(orderId, rateId);
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
