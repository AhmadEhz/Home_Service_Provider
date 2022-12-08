package org.homeservice.service.impl;

import lombok.NonNull;
import org.homeservice.entity.*;
import org.homeservice.repository.BidRepository;
import org.homeservice.repository.OrderRepository;
import org.homeservice.repository.SpecialistRepository;
import org.homeservice.repository.impl.BidRepositoryImpl;
import org.homeservice.repository.impl.OrderRepositoryImpl;
import org.homeservice.repository.impl.SpecialistRepositoryImpl;
import org.homeservice.service.*;
import org.homeservice.service.base.BaseServiceImpl;
import org.homeservice.util.exception.*;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class SpecialistServiceImpl extends BaseServiceImpl<Specialist, Long, SpecialistRepository>
        implements SpecialistService {
    private static SpecialistService service;
    private final BidRepository bidRepository;
    private final OrderRepository orderRepository;
    private final ServiceService serviceService;
    private final SubServiceService subServiceService;

    private SpecialistServiceImpl() {
        super(SpecialistRepositoryImpl.getRepository());
        bidRepository = BidRepositoryImpl.getRepository();
        orderRepository = OrderRepositoryImpl.getRepository();
        serviceService = ServiceServiceImpl.getService();
        subServiceService = SubServiceServiceImpl.getService();
    }

    @Override
    public List<Specialist> loadNewSpecialists() {
        return repository.findAll(SpecialistStatus.NEW);
    }

    @Override
    public List<Specialist> loadVerifiedSpecialists() {
        return repository.findAll(SpecialistStatus.ACCEPTED);
    }

    @Override
    public void changeStatus(Long specialistId, SpecialistStatus status) {
        int update = repository.changeStatus(specialistId, status);
        if (update < 1)
            throw new CustomIllegalArgumentException("Specialist with this id not found.");
    }

    @Override
    public void updateScore(Long id) {
        AtomicInteger update = new AtomicInteger();
        executeUpdate(() -> update.set(repository.updateScore(id)));
        if (update.get() < 1)
            throw new NotFoundException("Specialist not found.");
    }

    @Override
    public void setBid(Long specialistId, @NonNull Double offerPrice, @NonNull Duration timeSpent, Long orderId) {
        Optional<Specialist> optionalSpecialist = loadById(specialistId);
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalSpecialist.isEmpty())
            throw new NotFoundException("Specialist not found");

        if(optionalOrder.isEmpty())
            throw new NotFoundException("Order not found");

        if (optionalSpecialist.get().getStatus() != SpecialistStatus.ACCEPTED)
            throw new NotVerifiedException("Specialist not verified");

        Bid bid = new Bid(offerPrice, timeSpent, optionalSpecialist.get(),optionalOrder.get());
        checkEntity(bid);
        bidRepository.save(bid);
    }

    public static SpecialistService getService() {
        if (service == null)
            service = new SpecialistServiceImpl();
        return service;
    }
}
