package org.homeservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Entity
public class Specialist extends Person {

    @Email
    @Column(nullable = false, unique = true)
    @NotNull
    private String email;
    @Enumerated(value = EnumType.STRING)
    private SpecialistStatus status;

    @ColumnDefault("0")
    private Double score;

    @OneToMany(mappedBy = "specialist")
    private Set<Bid> bids;

    @OneToMany(mappedBy = "specialist")
    private Set<Order> orders;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Credit credit;

    //    @Max(307200)
//    @Length(max = 307200)// 300KB
    private byte[] avatar;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Transient
    private List<Rate> rates;

    {
        bids = new HashSet<>();
        orders = new HashSet<>();
    }

    public Specialist() {
    }

    public Specialist(Long id) {
        setId(id);
    }

    public Specialist(String firstName, String lastName, String username, String password, String email) {
        super(firstName, lastName, username, password);
        this.email = email;
    }

    public Specialist(String firstName, String lastName, Double score, byte[] avatar) {
        super(firstName, lastName);
        this.score = score;
        this.avatar = avatar;
    }

    public Specialist(String firstName, String lastName, String username, String password, String email, byte[] avatar) {
        super(firstName, lastName, username, password);
        this.email = email;
        this.avatar = avatar;
    }

    @PrePersist
    private void perPersist() {
        status = SpecialistStatus.NEW;
        credit = new Credit(0);
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

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
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

    public boolean setAvatar(File file) {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            avatar = fileInputStream.readAllBytes();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean setAvatar(MultipartFile file) {
        try {
            avatar = file.getBytes();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public List<Rate> getRates() {
        return rates;
    }

    public void setRates(List<Rate> rates) {
        this.rates = rates;
    }

    public boolean isVerified() {
        return status == SpecialistStatus.ACCEPTED;
    }

    @Override
    public String toString() {
        return "Specialist{" +
               "email='" + email + '\'' +
               ", status=" + status +
               ", score=" + score +
               ", credit=" + credit +
               ", avatar=" + Arrays.toString(avatar) +
               ", createdAt=" + createdAt +
               ", rates=" + rates +
               "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Specialist that = (Specialist) o;
        return Objects.equals(email, that.email) && status == that.status && Objects.equals(score, that.score)
               && Objects.equals(credit, that.credit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), email, status, score, credit, createdAt);
    }
}
