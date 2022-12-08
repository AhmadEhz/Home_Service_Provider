package org.homeservice.entity;

import jakarta.persistence.*;
import org.homeservice.entity.id.ServiceSpecialistId;

import java.util.Set;

@Entity
@IdClass(ServiceSpecialistId.class)
public class ServiceSpecialist {
    @Id
    @ManyToOne
    private Specialist specialist;
    @Id
    @ManyToOne
    private Service service;

    public ServiceSpecialist() {
    }

    public ServiceSpecialist(Specialist specialist, Service service) {
        this.specialist = specialist;
        this.service = service;
    }

    public Specialist getSpecialist() {
        return specialist;
    }

    public void setSpecialist(Specialist specialist) {
        this.specialist = specialist;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }
}
