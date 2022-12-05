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
}
