package org.homeservice.repository;

import org.homeservice.entity.Specialist;
import org.homeservice.entity.SpecialistStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpecialistRepository extends JpaRepository<Specialist, Long>, JpaSpecificationExecutor<Specialist> {

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
    @Query(nativeQuery = true, value = """
            with sp as (select sp.id as sp_id, avg(r.score - r.lateness_end_working) as avg_score
            from specialist sp join orders o on sp.id = o.specialist_id join rate r on r.id = o.rate_id
            where sp.id = (select sp2.id from specialist as sp2 join orders o2 on sp2.id = o2.specialist_id
            where o.rate_id = :rateId) group by sp.id)
            
            update specialist as s set score = sp.avg_score
            from spid where s.id = sp.sp_id""")
    void updateScoreByRateId(Long rateId);
}
