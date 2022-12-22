package org.homeservice.service.impl;

import org.homeservice.entity.Bid;
import org.homeservice.entity.Order;
import org.homeservice.entity.Specialist;
import org.homeservice.repository.BidRepository;
import org.homeservice.service.BidService;
import org.homeservice.service.OrderService;
import org.homeservice.service.SpecialistService;
import org.homeservice.service.base.BaseServiceImpl;
import org.homeservice.util.exception.CustomIllegalArgumentException;
import org.homeservice.util.exception.NotFoundException;
import org.homeservice.util.exception.NotVerifiedException;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Scope("singleton")
public class BidServiceImpl extends BaseServiceImpl<Bid, Long, BidRepository> implements BidService {
    private final SpecialistService specialistService;
    private final OrderService orderService;

    protected BidServiceImpl(BidRepository repository, SpecialistService specialistService, OrderService orderService) {
        super(repository);
        this.specialistService = specialistService;
        this.orderService = orderService;
    }

    @Override
    public void save(Bid bid, Long orderId, Long specialistId) {
        Order order = orderService.findById(orderId).orElseThrow(() -> new NotFoundException("Order not found"));
        Specialist specialist = specialistService.findById(specialistId).
                orElseThrow(() -> new NotFoundException("Specialist not found"));
        bid.setOrder(order);
        bid.setSpecialist(specialist);
        save(bid);
    }

    @Override
    public void save(Bid bid) {
        if (bid.getOrder() == null || bid.getOrder().getId() == null)
            throw new NullPointerException("Order or orderId is null.");
        if (bid.getSpecialist() == null || bid.getSpecialist().getId() == null)
            throw new NullPointerException("Specialist or specialistId is null.");

        if (!bid.getSpecialist().isVerified())
            throw new NotVerifiedException("Specialist is not verified yet or suspended.");
        if (bid.getOfferPrice() < bid.getOrder().getSubService().getBasePrice())
            throw new CustomIllegalArgumentException
                    ("Offer price should not be less than base price of the BaseService.");
        if (!bid.getOrder().checkStatusIfWaitingForBids()) //Check order not accepted a bid.
            throw new CustomIllegalArgumentException("This order did not accepted new bid.");

        super.save(bid);
        orderService.changeStatusToChooseSpecialist(bid.getOrder().getId(), bid.getOrder().getCustomer().getId());
    }

    @Override
    public List<Bid> loadAllByOrderSortedByPrice(Long orderId) {
        return repository.findAllByOrder_Id(orderId, Sort.by(Sort.Direction.ASC, "offerPrice"));
    }

    @Override
    public List<Bid> loadAllByOrderSortedBySpecialistScore(Long orderId) {
        return repository.findAllByOrder_Id(orderId, Sort.by(Sort.Direction.DESC, "specialist.score"));
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
