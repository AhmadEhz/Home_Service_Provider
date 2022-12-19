package org.homeservice.repository.hibernate;

import org.homeservice.entity.Specialist;
import org.homeservice.entity.SpecialistStatus;
import org.homeservice.repository.hibernate.base.HibernateBaseRepository;

import java.util.List;
import java.util.Optional;

public interface HibernateSpecialistRepository extends HibernateBaseRepository<Specialist, Long> {

    List<Specialist> findAll(SpecialistStatus status);

    int changeStatus(Long specialistId, SpecialistStatus status);

    int updateScore(Long id);

    Optional<Specialist> findByUsername(String username);

    Optional<Specialist> findByEmail(String email);
}
