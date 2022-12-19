package org.homeservice.repository;

import org.homeservice.entity.SubService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubServiceRepository extends JpaRepository<SubService, Long> {
//    @Query("""
//            select sub from SubServiceSpecialist as subsp
//            join fetch SubService as sub on sub.id = subsp.subService.id
//            where subsp.specialist.id = :specialistId""")
    @Query("""
            select s.subService from SubServiceSpecialist as s
            where s.specialist.id = :specialistId""")
    List<SubService> findAllBySpecialist(Long specialistId);

    List<SubService> findSubServicesByService_Id(Long serviceId);
}
