package org.homeservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;

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
               ", startWorkingTime=" + startWorkingTime +
               ", endWorkingTime=" + endWorkingTime +
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
               && Objects.equals(description, order.description) && Objects.equals(workingTime, order.workingTime)
               && Objects.equals(address, order.address) && Objects.equals(finalPrice, order.finalPrice)
               && Objects.equals(startWorkingTime, order.startWorkingTime)
               && Objects.equals(endWorkingTime, order.endWorkingTime) && status == order.status
               && Objects.equals(customer, order.customer) && Objects.equals(subService, order.subService)
               && Objects.equals(specialist, order.specialist) && Objects.equals(createdAt, order.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customerOfferPrice, description, workingTime, address, finalPrice, startWorkingTime, endWorkingTime, status, customer, subService, specialist, rate, createdAt);
    }
}
