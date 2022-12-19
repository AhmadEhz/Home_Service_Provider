package org.homeservice.service.impl;

import lombok.NonNull;
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
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

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
    public void save(@NonNull Bid bid) {
        if (bid.getOrder() == null)
            throw new NullPointerException("Order is null.");
        if (bid.getSpecialist() == null)
            throw new NullPointerException("Specialist is null.");

        if (bid.getOfferPrice() < bid.getOrder().getSubService().getBasePrice())
            throw new CustomIllegalArgumentException
                    ("Offer price should not be less than base price of the BaseService.");

        super.save(bid);
    }
}
