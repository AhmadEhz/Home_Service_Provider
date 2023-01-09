package org.homeservice.repository;

import org.homeservice.entity.Credit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CreditRepository extends JpaRepository<Credit, Long> {
    @Query("select c from Credit c join Specialist s where s.id = :specialistId")
    Optional<Credit> findBySpecialistId(Long specialistId);

    @Query("select c from Credit c join Customer cu where cu.id = :customerId")
    Optional<Credit> findByCustomerId(Long customerId);
}
