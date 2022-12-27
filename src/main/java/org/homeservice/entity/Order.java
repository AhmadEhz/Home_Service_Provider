package org.homeservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
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
    private LocalDateTime workingTime; //Customer offer working time

    @Column(nullable = false)
    @NotNull
    private String address;

    private Double finalPrice;

    private LocalDateTime startWorking;

    private LocalDateTime endWorking;

    private Duration latenessEndWorking;

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
                 String address) {
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

    public LocalDateTime getStartWorking() {
        return startWorking;
    }

    public void setStartWorking(LocalDateTime startWorkingTime) {
        this.startWorking = startWorkingTime;
    }

    public LocalDateTime getEndWorking() {
        return endWorking;
    }

    public void setEndWorking(LocalDateTime endWorkingTime) {
        this.endWorking = endWorkingTime;
    }

    public Duration getLatenessEndWorking() {
        return latenessEndWorking;
    }

    public void setLatenessEndWorking(Duration latenessEndWorking) {
        this.latenessEndWorking = latenessEndWorking;
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

    public boolean checkStatusIfWaitingForBids() {
        return status != OrderStatus.STARTED && status != OrderStatus.FINISHED && status != OrderStatus.PAID
               && status != OrderStatus.WAITING_FOR_COMING_SPECIALIST;
    }

    @Override
    public String toString() {
        return "Order{" +
               "id=" + id +
               ", customerOfferPrice=" + customerOfferPrice +
               ", description='" + description + '\'' +
               ", workingTime=" + workingTime +
               ", address='" + address + '\'' +
               ", finalPrice=" + finalPrice +
               ", startWorkingTime=" + startWorking +
               ", endWorkingTime=" + endWorking +
               ", status=" + status +
               ", customer=" + customer +
               ", subService=" + subService +
               ", specialist=" + specialist +
               ", rate=" + rate +
               ", createdAt=" + createdAt +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) && Objects.equals(customerOfferPrice, order.customerOfferPrice)
               && Objects.equals(description, order.description)
               && Objects.equals(address, order.address) && Objects.equals(finalPrice, order.finalPrice)
               && status == order.status && Objects.equals(customer, order.customer)
               && Objects.equals(subService, order.subService) && Objects.equals(specialist, order.specialist);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customerOfferPrice, description, workingTime, address,
                finalPrice, startWorking, endWorking, status, customer, subService, specialist, rate, createdAt);
    }
}
