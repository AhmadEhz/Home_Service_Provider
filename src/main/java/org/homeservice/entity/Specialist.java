package org.homeservice.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
public class Specialist extends Person {

    @Enumerated(value = EnumType.STRING)
    private SpecialistStatus status;

    @CreationTimestamp
    //todo: convert createdAt to created_at in db.
    private LocalDateTime createdAt;

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

    @Transient
    private List<Rate> rates;
}
