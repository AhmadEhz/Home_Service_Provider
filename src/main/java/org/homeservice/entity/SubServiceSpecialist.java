package org.homeservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.ManyToOne;
import org.homeservice.entity.id.SubServiceSpecialistId;

@Entity
@IdClass(SubServiceSpecialistId.class)
public class SubServiceSpecialist {
    @Id
    @ManyToOne
    private SubService subService;

    @Id
    @ManyToOne
    private Specialist specialist;
}
