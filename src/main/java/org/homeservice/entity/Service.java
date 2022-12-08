package org.homeservice.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;

    @OneToMany(cascade = CascadeType.PERSIST)
    private Set<SubService> subServices;

    @OneToMany(mappedBy = "service")
    private Set<ServiceSpecialist> specialists;

    {
        subServices = new HashSet<>();
        specialists = new HashSet<>();
    }

    public Service() {
    }

    public Service(String name) {
        this.name = name;
    }

    public Service(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<SubService> getSubServices() {
        return subServices;
    }

    public void setSubServices(Set<SubService> subServices) {
        this.subServices = subServices;
    }

    public void addSubService(SubService subService) {
        subServices.add(subService);
    }

    public Set<ServiceSpecialist> getSpecialists() {
        return specialists;
    }

    public void setSpecialists(Set<ServiceSpecialist> specialists) {
        this.specialists = specialists;
    }
}
