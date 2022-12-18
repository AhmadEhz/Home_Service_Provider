package org.homeservice.repository;

import org.homeservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByCustomer_Id(Long customerId);

    List<Order> findAllBySpecialist_Id(Long specialistId);
}
