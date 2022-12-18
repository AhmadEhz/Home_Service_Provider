package org.homeservice.repository;

import org.homeservice.entity.Specialist;
import org.homeservice.entity.SpecialistStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpecialistRepository extends JpaRepository<Specialist, Long> {

    List<Specialist> findSpecialistByStatus(SpecialistStatus status);

    @Modifying
    @Query("update Specialist set status = :status where id = :id")
    int updateStatus(Long id, SpecialistStatus status);

    @Modifying
    @Query("""
            update Specialist set score =
            (select avg(r.score) from Rate as r where r.id = :id)
            where id = :id""")
    int updateScore(Long id);
}
