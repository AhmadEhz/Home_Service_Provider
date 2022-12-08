package org.homeservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@ToString
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotNull
    private Double offerPrice;

    public Duration timeSpent;


    @ManyToOne
    @JoinColumn(nullable = false)
    @NotNull
    private Specialist specialist;

    @ManyToOne
    @JoinColumn(nullable = false)
    @NotNull
    private Order order;

    @CreationTimestamp
    private LocalDateTime createdAt;

    {
        timeSpent = Duration.ZERO;
    }

    public Bid() {
    }

    public Bid(Double offerPrice, Duration timeSpent, Specialist specialist, Order order) {
        this.offerPrice = offerPrice;
        this.timeSpent = timeSpent;
        this.specialist = specialist;
        this.order = order;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(Double offerPrice) {
        this.offerPrice = offerPrice;
    }

    public Duration getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(Duration timeSpent) {
        this.timeSpent = timeSpent;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Specialist getSpecialist() {
        return specialist;
    }

    public void setSpecialist(Specialist specialist) {
        this.specialist = specialist;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
