package org.homeservice.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "service", cascade = CascadeType.PERSIST)
    private Set<SubService> subServices;

    {
        subServices = new HashSet<>();
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

    @Override
    public String toString() {
        return "Service{" +
               "id=" + id +
               ", name='" + name + '\'' +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Service service = (Service) o;
        return Objects.equals(id, service.id) && Objects.equals(name, service.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
