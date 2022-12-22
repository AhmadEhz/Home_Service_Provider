package org.homeservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;
import java.util.Set;

@Entity
public class SubService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @NotNull
    private String name;

    @Column(columnDefinition = "text", nullable = false)
    @NotNull
    private String description;

    private Double basePrice;

    @ManyToOne
    @JoinColumn(nullable = false)
    @NotNull(message = "Service must be set")
    private Service service;

    public SubService() {
    }

    public SubService(Long id) {
        this.id = id;
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

    public Double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    @Override
    public String toString() {
        return "SubService{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", description='" + description + '\'' +
               ", basePrice=" + basePrice +
               ", service=" + service +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubService that = (SubService) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name)
               && Objects.equals(description, that.description) && Objects.equals(basePrice, that.basePrice)
               && Objects.equals(service, that.service);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, basePrice, service);
    }
}
