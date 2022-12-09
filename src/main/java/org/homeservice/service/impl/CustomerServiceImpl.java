package org.homeservice.service.impl;

import org.homeservice.entity.*;
import org.homeservice.repository.*;
import org.homeservice.repository.impl.*;
import org.homeservice.service.*;
import org.homeservice.service.base.BaseServiceImpl;
import org.homeservice.util.exception.CustomIllegalArgumentException;
import org.homeservice.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class CustomerServiceImpl extends BaseServiceImpl<Customer, Long, CustomerRepository>
        implements CustomerService {
    private static CustomerService service;
    private final SpecialistService specialistService;
    private final OrderRepository orderRepository;
    private final RateRepository rateRepository;

    private CustomerServiceImpl() {
        super(CustomerRepositoryImpl.getRepository());
        specialistService = SpecialistServiceImpl.getService();
        orderRepository = OrderRepositoryImpl.getRepository();
        rateRepository = RateRepositoryImpl.getRepository();
    }

    @Override
    public Customer save(String firstName, String lastName, String username, String email, String password) {
        Customer customer = new Customer(firstName, lastName, username, password, email);
        if (isExistedUsername(username))
            throw new CustomIllegalArgumentException("Username is exist.");
        if (isExistedEmail(email))
            throw new CustomIllegalArgumentException("Email is exist.");

        checkEntity(customer);
        executeUpdate(() -> repository.save(customer));
        return customer;
    }

    @Override
    public Order saveOrder(String description, Double offerPrice, LocalDateTime workingTime, String address) {
        Order order = new Order(offerPrice, description, workingTime, address);
        checkEntity(order);
        executeUpdate(() -> orderRepository.save(order));
        return order;
    }

    @Override
    public List<Specialist> findAllVerifiedSpecialist() {
        return specialistService.loadVerifiedSpecialists();
    }

    @Override
    public Rate saveRate(Long customerId, Double score, String comment, Long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isEmpty())
            throw new NotFoundException("Order not found.");
        if (!optionalOrder.get().getCustomer().getId().equals(customerId))
            throw new IllegalArgumentException("This customer is not owner of this order");
        Rate rate = new Rate(score, comment, optionalOrder.get());
        checkEntity(rate);
        executeUpdate(() -> rateRepository.save(rate));
        return rate;
    }

    @Override
    public Rate saveRate(Long customerId, Double score, Long orderId) {
        return saveRate(customerId, score, null, orderId);
    }

    @Override
    public void changePassword(Long id, String oldPassword, String newPassword) {
        Optional<Customer> optionalCustomer = loadById(id);
        if (optionalCustomer.isEmpty())
            throw new NotFoundException("Customer not found.");
        if (optionalCustomer.get().getPassword().equals(oldPassword))
            throw new CustomIllegalArgumentException("Password is not match");

        optionalCustomer.get().setPassword(newPassword);
        executeUpdate(() -> repository.update(optionalCustomer.get()));
    }

    @Override
    public boolean isExistedUsername(String username) {
        return repository.findByUsername(username).isPresent();
    }

    @Override
    public boolean isExistedEmail(String email) {
        return repository.findByEmail(email).isPresent();
    }

    public static CustomerService getService() {
        if (service == null)
            service = new CustomerServiceImpl();
        return service;
    }
}
