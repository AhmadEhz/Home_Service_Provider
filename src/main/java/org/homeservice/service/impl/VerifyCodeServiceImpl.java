package org.homeservice.service.impl;

import org.apache.commons.lang3.RandomStringUtils;
import org.homeservice.entity.VerifyCode;
import org.homeservice.repository.VerifyCodeRepository;
import org.homeservice.service.CustomerService;
import org.homeservice.service.SpecialistService;
import org.homeservice.service.VerifyCodeService;
import org.homeservice.service.base.BaseServiceImpl;
import org.homeservice.util.Values;
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

    private VerifyCode generateAndSave() {
        String generatedCode = RandomStringUtils.randomAlphabetic(Values.VERIFY_CODE_LENGTH);
        VerifyCode verifyCode = new VerifyCode(generatedCode);
        super.save(verifyCode);
        return verifyCode;
    }
}
