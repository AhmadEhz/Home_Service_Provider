package org.homeservice.repository;

import org.homeservice.entity.Bid;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {
    List<Bid> findAllByOrder_Id(Long orderId, Sort sort);
}
