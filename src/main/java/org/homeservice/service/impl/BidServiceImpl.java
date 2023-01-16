package org.homeservice.service.impl;

import org.homeservice.entity.Bid;
import org.homeservice.entity.Order;
import org.homeservice.entity.OrderStatus;
import org.homeservice.entity.Specialist;
import org.homeservice.repository.BidRepository;
import org.homeservice.service.BidService;
import org.homeservice.service.OrderService;
import org.homeservice.service.SpecialistService;
import org.homeservice.service.SubServiceSpecialistService;
import org.homeservice.service.base.BaseServiceImpl;
import org.homeservice.util.QueryUtil;
import org.homeservice.util.exception.CustomIllegalArgumentException;
import org.homeservice.util.exception.NotFoundException;
import org.homeservice.util.exception.SpecialistNotAccessException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Scope("singleton")
public class BidServiceImpl extends BaseServiceImpl<Bid, Long, BidRepository> implements BidService {
    private final SpecialistService specialistService;
    private final OrderService orderService;
    private final SubServiceSpecialistService subServiceSpecialistService;

    protected BidServiceImpl(BidRepository repository, SpecialistService specialistService, OrderService orderService, SubServiceSpecialistService subServiceSpecialistService) {
        super(repository);
        this.specialistService = specialistService;
        this.orderService = orderService;
        this.subServiceSpecialistService = subServiceSpecialistService;
    }

    @Override
    public void save(Bid bid, Long orderId, Specialist specialist) {
        Order order = orderService.loadById(orderId).orElseThrow(() -> new NotFoundException("Order not found"));
        bid.setOrder(order);
        bid.setSpecialist(specialist);
        save(bid);
    }

    @Override
    @Transactional
    public void save(Bid bid) {
        validate(bid);
        if (bid.getOrder() == null || bid.getOrder().getId() == null)
            throw new NullPointerException("Order or orderId is null.");
        if (bid.getSpecialist() == null || bid.getSpecialist().getId() == null)
            throw new NullPointerException("Specialist or specialistId is null.");

        Order order = bid.getOrder();
        if (!subServiceSpecialistService.isExist(bid.getSpecialist().getId(), order.getSubService().getId()))
            throw new SpecialistNotAccessException("Specialist not registered to this SubService.");
        if (!bid.getSpecialist().isVerified())
            throw new SpecialistNotAccessException("Specialist is not verified yet or suspended.");
        if (bid.getOfferPrice() < order.getSubService().getBasePrice())
            throw new CustomIllegalArgumentException
                    ("Offer price should not be less than base price of the BaseService.");
        if (!order.checkStatusIfWaitingForBids()) //Check order not accepted a bid.
            throw new CustomIllegalArgumentException("This order did not accepted new bid.");

        super.save(bid);
        if (order.getStatus() == OrderStatus.WAITING_FOR_BID)
            orderService.changeStatusToChooseSpecialist(order, order.getCustomer());
    }

    @Override
    public List<Bid> loadAllByOrder(Long orderId, String sortBy) {
        return repository.findAllByOrder_Id(orderId, QueryUtil.sortBy(sortBy));
    }

    @Override
    public Optional<Bid> loadByCustomerAndSpecialist(Long customerId, Long specialistId) {
        return repository.findByCustomerAndSpecialist(customerId, specialistId);
    }

    @Override
    public Optional<Bid> loadByOrderId(Long orderId) {
        return repository.findByOrderId(orderId);
    }
}
