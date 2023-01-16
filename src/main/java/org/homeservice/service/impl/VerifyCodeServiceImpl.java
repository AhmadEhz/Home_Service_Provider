package org.homeservice.service.impl;

import org.apache.commons.lang3.RandomStringUtils;
import org.homeservice.entity.VerifyCode;
import org.homeservice.repository.VerifyCodeRepository;
import org.homeservice.service.*;
import org.homeservice.service.base.BaseServiceImpl;
import org.homeservice.util.Values;
import org.homeservice.util.exception.CustomIllegalArgumentException;
import org.homeservice.util.exception.NotFoundException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Scope("singleton")
public class VerifyCodeServiceImpl extends BaseServiceImpl<VerifyCode, Long, VerifyCodeRepository>
        implements VerifyCodeService {
    private final CustomerService customerService;
    private final SpecialistService specialistService;

    protected VerifyCodeServiceImpl(VerifyCodeRepository repository, CustomerService customerService,
                                    SpecialistService specialistService) {
        super(repository);
        this.customerService = customerService;
        this.specialistService = specialistService;
    }

    @Override
    @Transactional
    public String generateAndSaveForCustomer(Long customerId) {
        VerifyCode verifyCode = generateAndSave();
        customerService.setVerificationId(customerId, verifyCode.getId());
        return verifyCode.getCode();
    }

    @Override
    @Transactional
    public String generateAndSaveForSpecialist(Long specialistId) {
        VerifyCode verifyCode = generateAndSave();
        specialistService.setVerificationId(specialistId, verifyCode.getId());
        return verifyCode.getCode();
    }

    @Override
    @Transactional
    public void verifySpecialistEmail(String verificationCode) {
        VerifyCode verifyCode = repository.findByCodeForSpecialist(verificationCode)
                .orElseThrow(() -> new NotFoundException("Verification code is not correct."));
        verifyEmail(verifyCode);
        specialistService.changeStatusToWaitingByVerificationCode(verificationCode);
    }

    @Override
    @Transactional
    public void verifyCustomerEmail(String verificationCode) {
        VerifyCode verifyCode = repository.findByCodeForCustomer(verificationCode)
                .orElseThrow(() -> new NotFoundException("Verification code is not correct."));
        verifyEmail(verifyCode);
    }

    private void verifyEmail(VerifyCode verifyCode) {
        if (verifyCode.isVerified())
            throw new CustomIllegalArgumentException("Verification code is used before.");
        verifyCode.setVerified(true);
        super.update(verifyCode);
    }

    private VerifyCode generateAndSave() {
        String generatedCode = RandomStringUtils.randomAlphanumeric(Values.VERIFY_CODE_LENGTH);
        VerifyCode verifyCode = new VerifyCode(generatedCode);
        super.save(verifyCode);
        return verifyCode;
    }
}
