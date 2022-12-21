package org.homeservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Objects;

@Entity
public class Rate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Positive(message = "Rate score should be positive")
    @Max(value = 5,message = "Max of score rating is 5")
    private double score;

    private String comment;

    @OneToOne(mappedBy = "rate")
    @JoinColumn(nullable = false)
    @NotNull
    private Order order;

    public Rate(double score, String comment, Order order) {
        this.score = score;
        this.comment = comment;
        this.order = order;
    }

    public Rate() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "Rate{" +
               "id=" + id +
               ", score=" + score +
               ", comment='" + comment + '\'' +
               ", order=" + order +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rate rate = (Rate) o;
        return Double.compare(rate.score, score) == 0 && Objects.equals(id, rate.id)
               && Objects.equals(comment, rate.comment) && Objects.equals(order, rate.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, score, comment, order);
    }
}
