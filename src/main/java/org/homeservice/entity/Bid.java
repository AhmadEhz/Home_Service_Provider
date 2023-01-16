package org.homeservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotNull
    private Double offerPrice;

    private String description;

    @Column(nullable = false)
    @NotNull(message = "Start working must not be null.")
    private LocalDateTime startWorking;

    @Column(nullable = false)
    @NotNull(message = "End working must not be null.")
    private LocalDateTime endWorking;

    @Transient
    private Duration timeSpent;


    @ManyToOne
    @JoinColumn(nullable = false)
    @NotNull
    private Specialist specialist;

    @ManyToOne
    @JoinColumn(nullable = false)
    @NotNull(message = "Order must not be null.")
    private Order order;

    @CreationTimestamp
    private LocalDateTime createdAt;

    {
        timeSpent = Duration.ZERO;
    }

    public Bid() {
    }

    public Bid(Double offerPrice, Specialist specialist, Order order, LocalDateTime startWorking, LocalDateTime endWorking) {
        this.offerPrice = offerPrice;
        this.specialist = specialist;
        this.order = order;
        this.startWorking = startWorking;
        this.endWorking = endWorking;
    }

    public Bid(Double offerPrice, LocalDateTime startWorking, LocalDateTime endWorking) {
        this.offerPrice = offerPrice;
        this.startWorking = startWorking;
        this.endWorking = endWorking;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStartWorking() {
        return startWorking;
    }

    public void setStartWorking(LocalDateTime startWorking) {
        this.startWorking = startWorking;
    }

    public LocalDateTime getEndWorking() {
        return endWorking;
    }

    public void setEndWorking(LocalDateTime endWorking) {
        this.endWorking = endWorking;
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

    @Override
    public String toString() {
        return "Bid{" +
               "id=" + id +
               ", offerPrice=" + offerPrice +
               ", startWorking=" + startWorking +
               ", endWorking=" + endWorking +
               ", timeSpent=" + timeSpent +
               ", specialist=" + specialist +
               ", order=" + order +
               ", createdAt=" + createdAt +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bid bid = (Bid) o;
        return Objects.equals(id, bid.id) && Objects.equals(offerPrice, bid.offerPrice)
               && Objects.equals(specialist, bid.specialist) && Objects.equals(order, bid.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, offerPrice, specialist, order);
    }
}
