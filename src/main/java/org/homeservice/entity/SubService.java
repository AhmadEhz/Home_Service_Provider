package org.homeservice.entity;

import jakarta.persistence.*;
import lombok.ToString;

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

    @ManyToOne
    @JoinColumn(nullable = false)
    private Service service;

    @OneToMany(mappedBy = "subService")
    private Set<SubServiceSpecialist> specialists;

    public SubService() {
    }

    public SubService(String name, String description, double basePrice, Service service) {
        this.name = name;
        this.description = description;
        this.basePrice = basePrice;
        this.service = service;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public Set<SubServiceSpecialist> getSpecialists() {
        return specialists;
    }

    public void setSpecialists(Set<SubServiceSpecialist> specialists) {
        this.specialists = specialists;
    }
}
