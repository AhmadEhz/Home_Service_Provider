package org.homeservice.service;

import org.homeservice.entity.SubServiceSpecialist;
import org.homeservice.entity.id.SubServiceSpecialistId;
import org.homeservice.service.base.BaseService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface SubServiceSpecialistService extends BaseService<SubServiceSpecialist, SubServiceSpecialistId> {
    Optional<SubServiceSpecialist> loadById(Long specialistId, Long subServiceId);

    boolean isExist(Long specialistId, Long subServiceId);

    void save(Long specialistId, Long subServiceId);

    void delete(Long specialistId, Long subServiceId);
}
