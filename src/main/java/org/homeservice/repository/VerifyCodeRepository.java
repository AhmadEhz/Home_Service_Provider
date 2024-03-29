package org.homeservice.repository;

import org.homeservice.entity.VerifyCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerifyCodeRepository extends JpaRepository<VerifyCode, Long> {
    @Query("select v from VerifyCode v inner join Specialist s on s.verifyCode.id=v.id where v.code = :code")
    Optional<VerifyCode> findByCodeForSpecialist(String code);

    @Query("select v from VerifyCode v inner join Customer c on c.verifyCode.id=v.id where v.code =:code")
    Optional<VerifyCode> findByCodeForCustomer(String code);
}
