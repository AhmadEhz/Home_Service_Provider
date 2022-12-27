package org.homeservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.hibernate.annotations.ColumnDefault;

import java.time.Duration;
import java.util.Objects;

@Entity
public class Rate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @PositiveOrZero(message = "Rate score should be positive")
    @Max(value = 5, message = "Max of score rating is 5")
    private Integer score;

    private String comment;

    private Integer latenessEndWorking;

    @OneToOne(mappedBy = "rate")
    @JoinColumn(nullable = false)
    @NotNull
    private Order order;

    public Rate() {
    }

    public Rate(Integer latenessEndWorking) {
        this.latenessEndWorking = latenessEndWorking;
    }

    public Rate(Integer score, String comment) {
        this.score = score;
        this.comment = comment;
    }

    public Rate(Integer score, String comment, Order order) {
        this.score = score;
        this.comment = comment;
        this.order = order;
    }

    @PrePersist
    void prePersist() {
        if(score == null)
            score = 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getLatenessEndWorking() {
        return latenessEndWorking;
    }

    public void setLatenessEndWorking(Integer latenessEndWorking) {
        this.latenessEndWorking = latenessEndWorking;
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
