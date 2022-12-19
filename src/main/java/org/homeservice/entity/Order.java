package org.homeservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    private Double customerOfferPrice;

    @Column(columnDefinition = "text", nullable = false)
    @NotNull
    private String description;

    @Column(nullable = false)
    @NotNull
    private LocalDateTime workingTime;

    @Column(nullable = false)
    @NotNull
    private String address;

    private Double finalPrice;

    private LocalDateTime startWorkingTime;

    private LocalDateTime endWorkingTime;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private OrderStatus status;

    @ManyToOne
    @JoinColumn(nullable = false)
    @NotNull
    private Customer customer;

    @OneToMany(mappedBy = "order")
    private Set<Bid> bids;

    @ManyToOne
    @JoinColumn(nullable = false)
    @NotNull
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
        status = OrderStatus.WAITING_FOR_BID;
    }

    public Order() {
    }


    public Order(Double customerOfferPrice, String description, LocalDateTime workingTime,
                 String address, SubService subService) {
        this.customerOfferPrice = customerOfferPrice;
        this.description = description;
        this.workingTime = workingTime;
        this.address = address;
        this.subService = subService;
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
