package org.homeservice.service.hibernate.impl;

import lombok.NonNull;
import org.homeservice.entity.*;
import org.homeservice.repository.hibernate.HibernateBidRepository;
import org.homeservice.repository.hibernate.HibernateOrderRepository;
import org.homeservice.repository.hibernate.HibernateSpecialistRepository;
import org.homeservice.repository.hibernate.impl.*;
import org.homeservice.service.hibernate.base.HibernateBaseServiceImpl;
import org.homeservice.service.hibernate.HibernateSpecialistService;
import org.homeservice.util.Values;
import org.homeservice.util.exception.*;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class HibernateSpecialistServiceImpl extends HibernateBaseServiceImpl<Specialist, Long, HibernateSpecialistRepository>
        implements HibernateSpecialistService {
    private static HibernateSpecialistService service;
    private final HibernateBidRepository bidRepository;
    private final HibernateOrderRepository orderRepository;

    private HibernateSpecialistServiceImpl() {
        super(HibernateSpecialistRepositoryImpl.getRepository());
        bidRepository = HibernateBidRepositoryImpl.getRepository();
        orderRepository = HibernateOrderRepositoryImpl.getRepository();
    }

    @Override
    public Specialist save(String firstName, String lastName, String username,
                           String email, String password, File avatar) {
        if (avatar.length() > Values.MAX_AVATAR_SIZE) //avatar size > 300KB
            throw new CustomIllegalArgumentException("Image size is bigger than " + (Values.MAX_AVATAR_SIZE/1024) + " KB");

        if(!avatar.getName().toLowerCase().endsWith(Values.AVATAR_FORMAT))
            throw new CustomIllegalArgumentException("Image format must be "+ Values.AVATAR_FORMAT);
        if (isExistEmail(email))
            throw new CustomIllegalArgumentException("Email is exist.");
        if (isExistUsername(username))
            throw new CustomIllegalArgumentException("Username is exist");

        Specialist specialist = new Specialist(firstName, lastName, username, password, email);
        specialist.setAvatar(avatar);
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
    public void setBid(Long specialistId, @NonNull Double offerPrice
            , LocalDateTime startWorking, LocalDateTime endWorking, Long orderId) {
        Optional<Specialist> optionalSpecialist = loadById(specialistId);
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalSpecialist.isEmpty())
            throw new NotFoundException("Specialist not found");
        if (optionalOrder.isEmpty())
            throw new NotFoundException("Order not found");
        if (optionalSpecialist.get().getStatus() != SpecialistStatus.ACCEPTED)
            throw new SpecialistNotAccessException("Specialist not verified");
        if(offerPrice < optionalOrder.get().getSubService().getBasePrice())
            throw new CustomIllegalArgumentException("Your bid is lower than base price of this SubService.");

        Bid bid = new Bid(offerPrice, optionalSpecialist.get(), optionalOrder.get(),startWorking,endWorking);
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

    public static HibernateSpecialistService getService() {
        if (service == null)
            service = new HibernateSpecialistServiceImpl();
        return service;
    }
}
