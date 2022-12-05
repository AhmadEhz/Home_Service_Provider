package org.homeservice.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
public class Customer extends Person {

    private String address;

    @Column(unique = true)
    private String email;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "customer")
    private Set<Order> orders;

    @OneToOne
    private Credit credit;

    @Transient
    private List<Rate> rates;

    {
        rates = new ArrayList<>();
    }
}
