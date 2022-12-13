package org.homeservice.repository;

import org.homeservice.entity.SubService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubServiceRepository extends JpaRepository<SubService, Long> {
    @Query("""
            select sub from SubServiceSpecialist as subsp
            join fetch SubService as sub on sub.id = subsp.subService.id
            where subsp.specialist.id = :specialistId""")
    List<SubService> findAllBySpecialist(Long specialistId);
}
