package org.homeservice.service.hibernate.impl;

import lombok.NonNull;
import org.homeservice.entity.*;
import org.homeservice.repository.hibernate.BidRepository;
import org.homeservice.repository.hibernate.OrderRepository;
import org.homeservice.repository.hibernate.SpecialistRepository;
import org.homeservice.repository.hibernate.impl.*;
import org.homeservice.service.hibernate.base.BaseServiceImpl;
import org.homeservice.service.hibernate.SpecialistService;
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

    private SpecialistServiceImpl() {
        super(SpecialistRepositoryImpl.getRepository());
        bidRepository = BidRepositoryImpl.getRepository();
        orderRepository = OrderRepositoryImpl.getRepository();
    }

    @Override
    public Specialist save(String firstName, String lastName, String username,
                           String email, String password, @NonNull byte[] avatar) {
        if (avatar.length > 300 * 1024) //avatar size > 300KB
            throw new CustomIllegalArgumentException("Image size is bigger than 300KB.");

        if (isExistEmail(email))
            throw new CustomIllegalArgumentException("Email is exist.");
        if (isExistUsername(username))
            throw new CustomIllegalArgumentException("Username is exist");

        Specialist specialist = new Specialist(firstName, lastName, username, password, email, avatar);
        save(specialist);
        return specialist;
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
        if (optionalOrder.isEmpty())
            throw new NotFoundException("Order not found");
        if (optionalSpecialist.get().getStatus() != SpecialistStatus.ACCEPTED)
            throw new NotVerifiedException("Specialist not verified");
        if(offerPrice < optionalOrder.get().getSubService().getBasePrice())
            throw new CustomIllegalArgumentException("Your bid is lower than base price of this SubService.");

        Bid bid = new Bid(offerPrice, timeSpent, optionalSpecialist.get(), optionalOrder.get());
        checkEntity(bid);
        bidRepository.save(bid);
    }

    @Override
    public boolean isExistUsername(String username) {
        return repository.findByUsername(username).isPresent();
    }

    @Override
    public boolean isExistEmail(String email) {
        return repository.findByEmail(email).isPresent();
    }

    @Override
    public void changePassword(Long id, String oldPassword, String newPassword) {
        Optional<Specialist> optionalSpecialist = loadById(id);
        if (optionalSpecialist.isEmpty())
            throw new NotFoundException("Specialist not found.");
        if (optionalSpecialist.get().getPassword().equals(oldPassword))
            throw new CustomIllegalArgumentException("Password is not match");

        optionalSpecialist.get().setPassword(newPassword);
        executeUpdate(() -> repository.update(optionalSpecialist.get()));
    }

    public static SpecialistService getService() {
        if (service == null)
            service = new SpecialistServiceImpl();
        return service;
    }
}
