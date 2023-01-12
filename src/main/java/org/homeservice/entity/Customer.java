package org.homeservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.*;

@Entity
public class Customer extends Person {

    @Column(unique = true, nullable = false)
    @Email
    @NotNull
    private String email;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "customer")
    private Set<Order> orders;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Credit credit;

    @OneToOne
    private VerifyCode verifyCode;

    @Transient
    private List<Rate> rates;

    //prevent NullPointerException
    {
        rates = new ArrayList<>();
        orders = new HashSet<>();
    }

    public Customer() {
    }

    public Customer(String firstName, String lastName, String username, String password, String email) {
        super(firstName, lastName, username, password);
        this.email = email;
    }
    @PrePersist
    void prePersist() {
        setRole(Role.CUSTOMER);
        credit = new Credit(0L);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    public Credit getCredit() {
        return credit;
    }

    public void setCredit(Credit credit) {
        this.credit = credit;
    }

    public List<Rate> getRates() {
        return rates;
    }

    public void setRates(List<Rate> rates) {
        this.rates = rates;
    }

    public VerifyCode getVerifyLink() {
        return verifyCode;
    }

    public void setVerifyLink(VerifyCode verifyCode) {
        this.verifyCode = verifyCode;
    }

    @Override
    public String toString() {
        return "Customer{" +
               "email='" + email + '\'' +
               ", credit=" + credit +
               super.toString() + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Customer customer = (Customer) o;
        return Objects.equals(email, customer.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), email);
    }
}
