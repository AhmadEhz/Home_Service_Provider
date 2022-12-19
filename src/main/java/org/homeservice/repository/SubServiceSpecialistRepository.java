package org.homeservice.repository;

import org.homeservice.entity.SubServiceSpecialist;
import org.homeservice.entity.id.SubServiceSpecialistId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubServiceSpecialistRepository extends JpaRepository<SubServiceSpecialist, SubServiceSpecialistId> {
    @Query("""
            select ss from SubServiceSpecialist as ss
            where ss.specialist.id = :specialistId and ss.subService.id = :subServiceId""")
    Optional<SubServiceSpecialist> findById(Long specialistId, Long subServiceId);

    @Modifying
    @Query("""
            delete from SubServiceSpecialist
            where Specialist.id = :id and subService.id = :subServiceId""")
    int removeFromSubService(Long id, Long subServiceId);

    @Modifying
    @Query(value = """
            insert into subService_specialist (specialist_id, subservice_id)
            values (:id, :subServiceId)""", nativeQuery = true)
    int addToSubService(Long id, Long subServiceId);
}
