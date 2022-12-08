package org.homeservice.service;

import lombok.NonNull;
import org.homeservice.entity.Specialist;
import org.homeservice.entity.SpecialistStatus;
import org.homeservice.service.base.BaseService;

import java.time.Duration;
import java.util.List;

public interface SpecialistService extends BaseService<Specialist,Long> {
    List<Specialist> loadNewSpecialists();

    List<Specialist> loadVerifiedSpecialists();

    void changeStatus(Long specialistId, SpecialistStatus status);

    void updateScore(Long id);

    void setBid(Long specialistId, @NonNull Double offerPrice, @NonNull Duration timeSpent,Long orderId);
}