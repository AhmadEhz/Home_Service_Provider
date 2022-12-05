package org.homeservice.entity;

import jakarta.persistence.*;

@Entity
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Specialist specialist;

    @ManyToOne
    private Order order;
}
