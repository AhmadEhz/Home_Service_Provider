package org.homeservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

import java.util.Set;

@Entity
public class Customer extends Person {
    @OneToMany(mappedBy = "customer")
    private Set<Rate> rates;
    @OneToMany(mappedBy = "customer")
    private Set<Order> orders;
    @OneToOne
    private Credit credit;
}
