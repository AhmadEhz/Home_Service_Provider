package org.homeservice.entity;

import jakarta.persistence.*;
import org.homeservice.entity.id.SubServiceSpecialistId;

import java.util.Objects;

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

    public SubServiceSpecialist(Long specialistId, Long subServiceId) {
        this.specialist = new Specialist(specialistId);
        this.subService = new SubService(subServiceId);
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

    @Override
    public String toString() {
        return "SubServiceSpecialist{" +
               "subService=" + subService +
               ", specialist=" + specialist +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubServiceSpecialist that = (SubServiceSpecialist) o;
        return Objects.equals(subService, that.subService) && Objects.equals(specialist, that.specialist);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subService, specialist);
    }
}
