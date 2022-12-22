package org.homeservice.repository;

import org.homeservice.entity.Specialist;
import org.homeservice.entity.SpecialistStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpecialistRepository extends JpaRepository<Specialist, Long> {

    List<Specialist> findSpecialistsByStatus(SpecialistStatus status);

    Optional<Specialist> findSpecialistByUsername(String username);

    Optional<Specialist> findSpecialistByEmail(String email);

    @Modifying
    @Query("update Specialist as s set s.status = :status where s.id = :id")
    int updateStatus(Long id, SpecialistStatus status);

    @Modifying
    @Query("""
            update Specialist as s set s.score =
            (select avg(r.score) from Rate as r where r.order.specialist.id = :id)
            where s.id = :id""")
    int updateScore(Long id);

    @Modifying
    @Query("""
            update Specialist as s set s.score =
            (select avg(r.score) from Rate as r where r.id = :rateId)
            where s.id = (select r.order.specialist.id from Rate as r where r.id = :rateId)""")
    int updateScoreByRateId(Long rateId);
}
