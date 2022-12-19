package org.homeservice.service.impl;

import org.homeservice.entity.SubServiceSpecialist;
import org.homeservice.entity.id.SubServiceSpecialistId;
import org.homeservice.repository.SubServiceSpecialistRepository;
import org.homeservice.service.SubServiceSpecialistService;
import org.homeservice.service.base.BaseServiceImpl;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Scope("singleton")
public class SubServiceSpecialistServiceImpl extends BaseServiceImpl<SubServiceSpecialist, SubServiceSpecialistId, SubServiceSpecialistRepository> implements SubServiceSpecialistService {
    protected SubServiceSpecialistServiceImpl(SubServiceSpecialistRepository repository) {
        super(repository);
    }

    @Override
    public Optional<SubServiceSpecialist> findById(Long specialistId, Long subServiceId) {
        return repository.findById(specialistId, subServiceId);
    }

    @Override
    public boolean isExist(Long specialistId, Long subServiceId) {
        return findById(specialistId, subServiceId).isPresent();
    }
}
