package org.homeservice.service.hibernate;

import lombok.NonNull;
import org.homeservice.entity.Specialist;
import org.homeservice.entity.SpecialistStatus;
import org.homeservice.service.hibernate.base.HibernateBaseService;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public interface HibernateSpecialistService extends HibernateBaseService<Specialist,Long> {

    Specialist save(String firstName, String lastName, String username,
                    String email, String password, File avatar);

    List<Specialist> loadNewSpecialists();

    List<Specialist> loadVerifiedSpecialists();

    void changeStatus(Long specialistId, SpecialistStatus status);

    void updateScore(Long id);

    void setBid(Long specialistId, @NonNull Double offerPrice
            , LocalDateTime startWorking, LocalDateTime endWorking, Long orderId);

    boolean isExistUsername(String username);

    boolean isExistEmail(String email);

    void changePassword(Long id, String oldPassword, String newPassword);
}