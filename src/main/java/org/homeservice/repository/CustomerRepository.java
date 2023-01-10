package org.homeservice.repository;

import org.homeservice.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {

    Optional<Customer> findCustomerByUsername(String username);

    Optional<Customer> findCustomerByEmail(String email);

    @Modifying
    @Query("update Customer c set c.verifyCode.id = :verificationId where c.id = :id")
    void setVerificationCodeId(Long id, Long verificationId);
}
