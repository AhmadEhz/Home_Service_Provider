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
    @Query("update Specialist set status = :status where id = :id")
    int updateStatus(Long id, SpecialistStatus status);

    @Modifying
    @Query("""
            update Specialist set score =
            (select avg(r.score) from Rate as r where r.order.specialist.id = :id)
            where id = :id""")
    int updateScore(Long id);

    @Modifying
    @Query("""
            update Specialist set score =
            (select avg(r.score) from Rate as r where r.id = :rateId)
            where id = (select r.order.specialist.id from Rate as r where r.id = :rateId)""")
    int updateScoreByRateId(Long rateId);

    @Modifying
    @Query(value = """
            insert into subService_specialist (specialist_id, subservice_id)
            values (:id, :subServiceId)""", nativeQuery = true)
    int addToSubService(Long id, Long subServiceId);

    @Modifying
    @Query("""
            delete from SubServiceSpecialist
            where Specialist.id = :id and subService.id = :subServiceId""")
    int removeFromSubService(Long id, Long subServiceId);
}
