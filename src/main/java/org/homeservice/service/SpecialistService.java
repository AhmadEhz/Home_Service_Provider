package org.homeservice.service;

import org.homeservice.entity.Specialist;
import org.homeservice.entity.SpecialistStatus;
import org.homeservice.service.base.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Map;

@Service
public interface SpecialistService extends BaseService<Specialist, Long> {
    void addAvatar(Specialist specialist, MultipartFile avatar);

    List<Specialist> loadAllVerified();

    List<Specialist> loadAllNew();

    List<Specialist> loadAllByFilter(Map<String, String> filters);

    List<Specialist> loadAllBySubService(Long subServiceId);

    void verifySpecialist(Long id);

    void suspendSpecialist(Long id);

    void changeStatusToWaitingByVerificationCode(String verificationCode);

    void changeStatus(Long id, SpecialistStatus status);

    void updateScore(Long id);

    void updateScoreByRateId(Long rateId);

    void setVerificationId(Long id, Long verificationId);

    void changePassword(Specialist specialist, String oldPassword, String newPassword);

    void addAvatar(Long id, File file);

    boolean isExistUsername(String username);

    boolean isExistEmail(String email);

    void deleteByAdmin(Specialist specialist);
}
