package org.homeservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    @NotNull
    private Credit credit;

    @ManyToOne
    private Credit destinationCredit;

    @Positive
    @NotNull
    @Column(nullable = false)
    private Long amount;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private TransactionType type;

    public Transaction() {
    }

    public Transaction(Credit credit, Long amount, TransactionType type) {
        this.credit = credit;
        this.amount = amount;
        this.type = type;
    }

    public Transaction(Credit credit, Credit destinationCredit, Long amount, TransactionType type) {
        this.credit = credit;
        this.destinationCredit = destinationCredit;
        this.amount = amount;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Credit getCredit() {
        return credit;
    }

    public void setCredit(Credit credit) {
        this.credit = credit;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime localDateTime) {
        this.createdAt = localDateTime;
    }

    public Credit getDestinationCredit() {
        return destinationCredit;
    }

    public void setDestinationCredit(Credit destinationCredit) {
        this.destinationCredit = destinationCredit;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }
}
