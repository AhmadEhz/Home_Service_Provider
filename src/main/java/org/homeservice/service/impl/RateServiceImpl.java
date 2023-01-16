package org.homeservice.service.impl;

import org.homeservice.entity.Customer;
import org.homeservice.entity.Order;
import org.homeservice.entity.Rate;
import org.homeservice.repository.RateRepository;
import org.homeservice.service.CustomerService;
import org.homeservice.service.OrderService;
import org.homeservice.service.RateService;
import org.homeservice.service.SpecialistService;
import org.homeservice.service.base.BaseServiceImpl;
import org.homeservice.util.exception.NotFoundException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Scope("singleton")
public class RateServiceImpl extends BaseServiceImpl<Rate, Long, RateRepository> implements RateService {

    private final SpecialistService specialistService;
    private final OrderService orderService;

    public RateServiceImpl(RateRepository repository, SpecialistService specialistService,
                           OrderService orderService) {
        super(repository);
        this.specialistService = specialistService;
        this.orderService = orderService;
    }

    @Override
    @Transactional
    public void save(Rate rate, Long orderId, Customer customer) {
        Order order = orderService.findById(orderId).orElseThrow(() -> new NotFoundException("Order not found."));
        if (!order.getCustomer().equals(customer))
            throw new IllegalArgumentException("This Order is not for this Customer.");

        rate.setOrder(order);
        rate.setLatenessEndWorking((int) order.getLatenessEndWorking().toHours());
        save(rate);
    }

    @Override
    @Transactional
    public void save(Rate rate) {
        validate(rate);
        if (rate.getOrder() == null || rate.getOrder().getId() == null)
            throw new NullPointerException("Order or OrderId is null.");
        super.save(rate);
        orderService.setRateId(rate.getOrder().getId(), rate.getId());
        specialistService.updateScoreByRateId(rate.getId());
    }
}
