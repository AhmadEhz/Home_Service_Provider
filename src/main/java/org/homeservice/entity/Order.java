package org.homeservice.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double customerOfferPrice;

    @Column(columnDefinition = "text")
    private String description;

    private LocalDateTime workingTime;

    private String address;

    private Double finalPrice;

    private LocalDateTime startWorkingTime;

    private LocalDateTime endWorkingTime;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus status;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @ManyToOne
    private Customer customer;

    @OneToMany(mappedBy = "order")
    private Set<Bid> bids;

    @ManyToOne
    private SubService subService;

    @ManyToOne
    private Specialist specialist;

    @OneToOne
    private Rate rate;

}
