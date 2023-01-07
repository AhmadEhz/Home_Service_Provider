package org.homeservice.repository;

import org.homeservice.entity.Order;
import org.homeservice.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> , JpaSpecificationExecutor<Order> {
    List<Order> findAllByCustomer_Id(Long customerId);

    List<Order> findAllBySpecialist_Id(Long specialistId);
    
    //If specialist is not null, It means Customer accepted a bid for this Order.
    @Query("select o from Order as o where o.specialist is not null")
    Optional<Order> findIfAcceptedABid(Long id);

    @Query("""
            select o from Order as o join SubServiceSpecialist as ssp on o.subService = ssp.subService
            where ssp.specialist.id = :specialistId""")
    List<Order> findBySpecialistSubServices(Long specialistId);

    @Query("select o from Order as o where o.specialist.id = :specialistId and o.status in :statuses")
    List<Order> findAllBySpecialistAndStatus(Long specialistId, OrderStatus[] statuses);

    @Modifying
    @Query("update Order as o set o.status = :status where o.id = :id")
    void changeStatus(Long id, OrderStatus status);

    @Modifying
    @Query("update Order as o set o.rate.id = :rateId where o.id = :orderId")
    void setRateId(Long orderId, Long rateId);
}
