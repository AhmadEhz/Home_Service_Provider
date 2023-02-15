package org.homeservice.service.impl;

import org.homeservice.entity.Specialist;
import org.homeservice.entity.SubService;
import org.homeservice.entity.SubServiceSpecialist;
import org.homeservice.entity.id.SubServiceSpecialistId;
import org.homeservice.repository.SubServiceSpecialistRepository;
import org.homeservice.service.SpecialistService;
import org.homeservice.service.SubServiceService;
import org.homeservice.service.SubServiceSpecialistService;
import org.homeservice.service.base.BaseServiceImpl;
import org.homeservice.util.exception.CustomIllegalArgumentException;
import org.homeservice.util.exception.NotFoundException;
import org.homeservice.util.exception.SpecialistNotAccessException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Scope("singleton")
public class SubServiceSpecialistServiceImpl extends BaseServiceImpl<SubServiceSpecialist, SubServiceSpecialistId,
        SubServiceSpecialistRepository> implements SubServiceSpecialistService {
    private final SpecialistService specialistService;
    private final SubServiceService subServiceService;

    protected SubServiceSpecialistServiceImpl(SubServiceSpecialistRepository repository,
                                              SpecialistService specialistService, SubServiceService subServiceService) {
        super(repository);
        this.specialistService = specialistService;
        this.subServiceService = subServiceService;
    }

    @Override
    public Optional<SubServiceSpecialist> loadById(Long specialistId, Long subServiceId) {
        return repository.findById(specialistId, subServiceId);
    }

    @Override
    public boolean isExist(Long specialistId, Long subServiceId) {
        return loadById(specialistId, subServiceId).isPresent();
    }

    @Override
    public void save(Long specialistId, Long subServiceId) {
        save(new SubServiceSpecialist(specialistId, subServiceId));
    }

    @Override
    public void save(SubServiceSpecialist subServiceSpecialist) {
        SubService subService = subServiceService.loadById(subServiceSpecialist.getSubService().getId()).orElseThrow(
                () -> new NotFoundException("SubService not found."));
        Specialist specialist = specialistService.loadById(subServiceSpecialist.getSpecialist().getId()).orElseThrow(
                () -> new NotFoundException("Specialist not found."));
        if (isExist(subService.getId(), specialist.getId()))
            throw new CustomIllegalArgumentException("This Specialist was added to this SubServiceBefore.");
        if (!specialist.isVerified())
            throw new SpecialistNotAccessException("Specialist not verified or suspended.");

        //No need to set SubService and Specialist to SubServiceSpecialist. both id was set to it before.
        super.save(subServiceSpecialist);
    }

    @Override
    @Transactional
    public void delete(Long specialistId, Long subServiceId) {
        if (!isExist(specialistId, subServiceId))
            throw new CustomIllegalArgumentException("This Specialist is not part of SubService.");
        repository.remove(specialistId, subServiceId);
    }
}
