package org.homeservice.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double customerOfferPrice;

    @Column(columnDefinition = "text")
    private String description;

    @Column(nullable = false)
    private LocalDateTime workingTime;

    @Column(nullable = false)
    private String address;

    private Double finalPrice;

    private LocalDateTime startWorkingTime;

    private LocalDateTime endWorkingTime;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus status;

    @ManyToOne
    private Customer customer;

    @OneToMany(mappedBy = "order")
    private Set<Bid> bids;

    @ManyToOne
    private SubService subService;

    @ManyToOne
    private Specialist specialist;

    @OneToOne
    private Rate rate;

    @CreationTimestamp
    private LocalDateTime createdAt;

    {
        bids = new HashSet<>();
    }

    @PrePersist
    private void prePersist() {
        status = OrderStatus.STARTED;
    }

    public Order() {
    }


    public Order(Double customerOfferPrice, String description, LocalDateTime workingTime, String address) {
        this.customerOfferPrice = customerOfferPrice;
        this.description = description;
        this.workingTime = workingTime;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getCustomerOfferPrice() {
        return customerOfferPrice;
    }

    public void setCustomerOfferPrice(Double customerOfferPrice) {
        this.customerOfferPrice = customerOfferPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getWorkingTime() {
        return workingTime;
    }

    public void setWorkingTime(LocalDateTime workingTime) {
        this.workingTime = workingTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(Double finalPrice) {
        this.finalPrice = finalPrice;
    }

    public LocalDateTime getStartWorkingTime() {
        return startWorkingTime;
    }

    public void setStartWorkingTime(LocalDateTime startWorkingTime) {
        this.startWorkingTime = startWorkingTime;
    }

    public LocalDateTime getEndWorkingTime() {
        return endWorkingTime;
    }

    public void setEndWorkingTime(LocalDateTime endWorkingTime) {
        this.endWorkingTime = endWorkingTime;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Set<Bid> getBids() {
        return bids;
    }

    public void setBids(Set<Bid> bids) {
        this.bids = bids;
    }

    public SubService getSubService() {
        return subService;
    }

    public void setSubService(SubService subService) {
        this.subService = subService;
    }

    public Specialist getSpecialist() {
        return specialist;
    }

    public void setSpecialist(Specialist specialist) {
        this.specialist = specialist;
    }

    public Rate getRate() {
        return rate;
    }

    public void setRate(Rate rate) {
        this.rate = rate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
