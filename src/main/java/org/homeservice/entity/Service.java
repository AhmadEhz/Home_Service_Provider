package org.homeservice.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany
    private Set<SubService> subServices;

    @OneToMany(mappedBy = "service")
    private Set<ServiceSpecialist> specialists;

}
