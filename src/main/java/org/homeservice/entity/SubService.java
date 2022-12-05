package org.homeservice.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class SubService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    private double basePrice;

    @OneToMany(mappedBy = "subService")
    private Set<SubServiceSpecialist> specialists;
}
