package org.homeservice.entity;

import jakarta.persistence.*;
import org.homeservice.entity.id.SubServiceSpecialistId;

@Entity
@Table(name = "subservice_specialist")
@IdClass(SubServiceSpecialistId.class)
public class SubServiceSpecialist {
    @Id
    @ManyToOne
    private SubService subService;

    @Id
    @ManyToOne
    private Specialist specialist;

    public SubServiceSpecialist() {
    }

    public SubServiceSpecialist(Specialist specialist, SubService subService) {
        this.subService = subService;
        this.specialist = specialist;
    }

    public SubService getSubService() {
        return subService;
    }

    public void setSubService(SubService subService) {
        this.subService = subService;
    }

    public Specialist getSpecialist() {
        return specialist;
    }

    public void setSpecialist(Specialist specialist) {
        this.specialist = specialist;
    }
}
