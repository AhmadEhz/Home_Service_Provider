package org.homeservice.entity;

import jakarta.persistence.*;

@Entity
public class Rate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double score;
    private String comment;

    @OneToOne(mappedBy = "rate")
    private Order order;
}
