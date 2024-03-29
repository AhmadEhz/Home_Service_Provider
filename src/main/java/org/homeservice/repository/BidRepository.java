package org.homeservice.repository;

import org.homeservice.entity.Bid;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {
    List<Bid> findAllByOrder_Id(Long orderId, Sort sort);

    @Query("select b from Bid as b where b.order.id = :orderId and b.specialist.id = :specialistId")
    Optional<Bid> findByOrderAndSpecialist(Long orderId, Long specialistId);
}
