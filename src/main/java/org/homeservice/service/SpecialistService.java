package org.homeservice.service;

import org.homeservice.entity.Specialist;
import org.homeservice.entity.SpecialistStatus;
import org.homeservice.service.base.BaseService;

import java.util.List;

public interface SpecialistService extends BaseService<Specialist,Long> {
    List<Specialist> loadNewSpecialists();

    void changeStatus(Long specialistId, SpecialistStatus status);
}
