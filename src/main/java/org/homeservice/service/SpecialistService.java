package org.homeservice.service;

import lombok.NonNull;
import org.homeservice.entity.Specialist;
import org.homeservice.entity.SpecialistStatus;
import org.homeservice.service.base.BaseService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public interface SpecialistService extends BaseService<Specialist, Long> {
    List<Specialist> loadAllVerified();

    List<Specialist> loadAllNew();

    void verifySpecialist(Long id);

    void suspendSpecialist(Long id);

    void changeStatus(Long id, SpecialistStatus status);

    void updateScore(Long id);

    void updateScoreByRateId(Long rateId);

    void changePassword(String username, String oldPassword, String newPassword);

    void addImage(Long id, File file);

    boolean isExistUsername(String username);

    boolean isExistEmail(String email);

    void checkStatusVerified(Long id);
}
