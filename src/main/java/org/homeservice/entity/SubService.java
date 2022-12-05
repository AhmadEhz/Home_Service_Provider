package org.homeservice.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class SubService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "subService")
    private Set<SubServiceSpecialist> specialists;
}
