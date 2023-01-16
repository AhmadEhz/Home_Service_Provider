package org.homeservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Credit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotNull(message = "Amount must not be null.")
    @PositiveOrZero(message = "Amount must not be negative")
    private Long amount;

    @OneToMany(mappedBy = "credit")
    private Set<Transaction> transactions;

    {
        transactions = new HashSet<>();
    }

    public Credit() {
    }

    public Credit(Long amount) {
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public boolean isSufficientAmount(Long withdrawalAmount) {
        return amount - withdrawalAmount >= 0;
    }

    public void deposit(Long depositAmount) {
        amount += depositAmount;
    }

    public void withdraw(Long withdrawalAmount) {
        amount -= withdrawalAmount;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    @Override
    public String toString() {
        return "Credit{" +
               "id=" + id +
               ", amount=" + amount +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Credit credit = (Credit) o;
        return Objects.equals(id, credit.id) && Objects.equals(amount, credit.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount);
    }
}
