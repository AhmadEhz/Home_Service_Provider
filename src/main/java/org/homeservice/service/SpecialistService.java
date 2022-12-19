package org.homeservice.service;

import lombok.NonNull;
import org.homeservice.entity.Specialist;
import org.homeservice.entity.SpecialistStatus;
import org.homeservice.service.base.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SpecialistService extends BaseService<Specialist, Long> {
    List<Specialist> loadAllVerified();

    List<Specialist> loadAllNew();

    void changeStatus(Long id, @NonNull SpecialistStatus status);

    void updateScore(Long id);

    void updateScoreByRateId(Long rateId);

    void addToSubService(Long id, Long subServiceId);

    void removeFromSubService(Long id, Long subServiceId);

    void changePassword(String username, String oldPassword, String newPassword);

    boolean isExistUsername(String username);

    boolean isExistEmail(String email);
}
