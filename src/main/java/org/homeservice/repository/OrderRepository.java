package org.homeservice.repository;

import org.homeservice.entity.Order;
import org.homeservice.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByCustomer_Id(Long customerId);

    List<Order> findAllBySpecialist_Id(Long specialistId);

    @Modifying
    @Query("update Order as o set o.status = :status where o.id = :id")
    int changeStatus(Long id, OrderStatus status);
}
