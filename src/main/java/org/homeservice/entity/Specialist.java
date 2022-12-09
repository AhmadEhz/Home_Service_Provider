package org.homeservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Specialist extends Person {

    @Email
    @Column(nullable = false)
    @NotNull
    private String email;
    @Enumerated(value = EnumType.STRING)
    private SpecialistStatus status;

    @OneToMany(mappedBy = "specialist")
    private Set<ServiceSpecialist> services;

    @ColumnDefault("0")
    private Double score;

    @OneToMany
    private Set<SubServiceSpecialist> subServices;

    @OneToMany(mappedBy = "specialist")
    private Set<Bid> bids;

    @OneToMany(mappedBy = "specialist")
    private Set<Order> orders;

    @OneToOne
    private Credit credit;

    private byte[] avatar;

    @Transient
    private List<Rate> rates;

    @CreationTimestamp
    private LocalDateTime createdAt;

    {
        services = new HashSet<>();
        bids = new HashSet<>();
        orders = new HashSet<>();
    }

    public Specialist() {
    }

    public Specialist(String firstName, String lastName, String username, String password, String email, byte[] avatar) {
        super(firstName, lastName, username, password);
        this.email = email;
        this.avatar = avatar;
    }

    @PrePersist
    private void perPersist() {
        status = SpecialistStatus.NEW;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public SpecialistStatus getStatus() {
        return status;
    }

    public void setStatus(SpecialistStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Set<ServiceSpecialist> getServices() {
        return services;
    }

    public void setServices(Set<ServiceSpecialist> services) {
        this.services = services;
    }

    public void addService(Service service) {
        services.add(new ServiceSpecialist(this, service));
    }

    public void removeService(Service service) {
        services.remove(new ServiceSpecialist(this, service));
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Set<SubServiceSpecialist> getSubServices() {
        return subServices;
    }

    public void setSubServices(Set<SubServiceSpecialist> subServices) {
        this.subServices = subServices;
    }

    public void addSubService(SubService subService) {
        subServices.add(new SubServiceSpecialist(this, subService));
    }

    public void removeSubService(SubService subService) {
        subServices.remove(new SubServiceSpecialist(this, subService));
    }

    public Set<Bid> getBids() {
        return bids;
    }

    public void setBids(Set<Bid> bids) {
        this.bids = bids;
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

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public List<Rate> getRates() {
        return rates;
    }

    public void setRates(List<Rate> rates) {
        this.rates = rates;
    }
}
