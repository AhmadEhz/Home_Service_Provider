package org.homeservice.service;

import org.homeservice.entity.VerifyCode;
import org.homeservice.service.base.BaseService;
import org.springframework.stereotype.Service;

@Service
public interface VerifyCodeService extends BaseService<VerifyCode,Long> {
    String generateAndSaveForCustomer(Long customerId);

    String generateAndSaveForSpecialist(Long specialistId);

    void verifySpecialistEmail(String verificationCode);

    void verifyCustomerEmail(String verificationCode);
}
