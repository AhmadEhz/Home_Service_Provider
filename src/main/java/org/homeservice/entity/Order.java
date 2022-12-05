package org.homeservice.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Customer customer;

    @OneToMany(mappedBy = "order")
    private Set<Bid> bids;

    @ManyToOne
    private Specialist specialist;

    @OneToOne
    private Rate rate;

}
