package org.homeservice.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double offerPrice;

    //todo: how to calculate time spent.
    private Duration timeSpent;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @ManyToOne
    private Specialist specialist;

    @ManyToOne
    private Order order;
}
