package org.homeservice.service.impl;

import org.homeservice.entity.SubServiceSpecialist;
import org.homeservice.entity.id.SubServiceSpecialistId;
import org.homeservice.repository.SubServiceSpecialistRepository;
import org.homeservice.service.SpecialistService;
import org.homeservice.service.SubServiceSpecialistService;
import org.homeservice.service.base.BaseServiceImpl;
import org.homeservice.util.exception.CustomIllegalArgumentException;
import org.homeservice.util.exception.NotFoundException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Scope("singleton")
public class SubServiceSpecialistServiceImpl extends BaseServiceImpl<SubServiceSpecialist, SubServiceSpecialistId, SubServiceSpecialistRepository> implements SubServiceSpecialistService {
    private final SpecialistService specialistService;
    protected SubServiceSpecialistServiceImpl(SubServiceSpecialistRepository repository,
                                              SpecialistService specialistService) {
        super(repository);
        this.specialistService = specialistService;
    }

    @Override
    public Optional<SubServiceSpecialist> findById(Long specialistId, Long subServiceId) {
        return repository.findById(specialistId, subServiceId);
    }

    @Override
    public boolean isExist(Long specialistId, Long subServiceId) {
        return findById(specialistId, subServiceId).isPresent();
    }

    @Override
    public void save(Long specialistId, Long subServiceId) {
        if (isExist(specialistId, subServiceId))
            throw new CustomIllegalArgumentException("This Specialist is part of SubService before.");
        specialistService.checkStatusVerified(specialistId);
        super.save(new SubServiceSpecialist(specialistId, subServiceId));
    }

    @Override
    @Transactional
    public void delete(Long specialistId, Long subServiceId) {
        if (!isExist(specialistId, subServiceId))
            throw new NotFoundException("This Specialist is not part of SubService.");
        repository.remove(specialistId, subServiceId);
    }
}
