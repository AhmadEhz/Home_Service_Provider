package org.homeservice.service;

import org.homeservice.entity.Specialist;
import org.homeservice.entity.SpecialistStatus;
import org.homeservice.service.base.BaseService;

import java.util.List;

public interface SpecialistService extends BaseService<Specialist,Long> {
    List<Specialist> loadNewSpecialists();

    List<Specialist> loadVerifiedSpecialists();

    void changeStatus(Long specialistId, SpecialistStatus status);

    void addToService(Long specialistId, Long serviceId);

    void addToSubService(Long specialistId, Long subServiceId);

    void removeFromService(Long specialistId, Long serviceId);

    void removeFromSubService(Long specialistId, Long subServiceId);
}
