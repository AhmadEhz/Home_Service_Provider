package org.homeservice.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Specialist extends Person {

    @OneToMany(mappedBy = "specialist")
    private Set<ServiceSpecialist> services;

    @OneToMany
    private Set<SubServiceSpecialist> subServices;

    @OneToMany(mappedBy = "specialist")
    private Set<Bid> bids;

    @OneToMany(mappedBy = "specialist")
    private Set<Rate> rates;

    @OneToMany(mappedBy = "specialist")
    private Set<Order> orders;

    @OneToOne
    private Credit credit;
}
