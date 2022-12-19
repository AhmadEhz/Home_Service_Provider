package org.homeservice.repository;

import org.homeservice.entity.SubServiceSpecialist;
import org.homeservice.entity.id.SubServiceSpecialistId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubServiceSpecialistRepository extends JpaRepository<SubServiceSpecialist, SubServiceSpecialistId> {
}
