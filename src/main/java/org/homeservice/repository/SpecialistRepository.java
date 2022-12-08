package org.homeservice.repository;

import org.homeservice.entity.Specialist;
import org.homeservice.entity.SpecialistStatus;
import org.homeservice.repository.base.BaseRepository;

import java.util.List;

public interface SpecialistRepository extends BaseRepository<Specialist, Long> {

    List<Specialist> findAll(SpecialistStatus status);

    int changeStatus(Long specialistId, SpecialistStatus status);

    int updateScore(Long id);
}
