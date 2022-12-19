package org.homeservice.service.impl;

import lombok.NonNull;
import org.homeservice.entity.Specialist;
import org.homeservice.entity.SpecialistStatus;
import org.homeservice.repository.SpecialistRepository;
import org.homeservice.service.SpecialistService;
import org.homeservice.service.SubServiceService;
import org.homeservice.service.base.BaseServiceImpl;
import org.homeservice.util.QueryUtil;
import org.homeservice.util.exception.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Scope("singleton")
public class SpecialistServiceImpl extends BaseServiceImpl<Specialist, Long, SpecialistRepository>
        implements SpecialistService {
    SubServiceService subServiceService;

    public SpecialistServiceImpl(SpecialistRepository repository, SubServiceService subServiceService) {
        super(repository);
        this.subServiceService = subServiceService;
    }

    @Override
    public void save(Specialist specialist) {
        if (isExistUsername(specialist.getUsername()))
            throw new NonUniqueException("Username is not unique.");
        if (isExistEmail(specialist.getEmail()))
            throw new NonUniqueException("Email is not unique.");
        super.save(specialist);
    }

    @Override
    public List<Specialist> loadAllVerified() {
        return repository.findSpecialistsByStatus(SpecialistStatus.ACCEPTED);
    }

    @Override
    public List<Specialist> loadAllNew() {
        return repository.findSpecialistsByStatus(SpecialistStatus.NEW);
    }

    @Override
    @Transactional
    public void changeStatus(Long id, @NonNull SpecialistStatus status) {
        int update = repository.updateStatus(id, status);
        QueryUtil.checkUpdate(update, () -> new NotFoundException("Specialist not found."));
    }

    @Override
    @Transactional
    public void updateScore(Long id) {
        int update = repository.updateScore(id);
        QueryUtil.checkUpdate(update, () -> new NotFoundException("Specialist not found."));
    }

    @Override
    @Transactional
    public void updateScoreByRateId(Long rateId) {
        int update = repository.updateScoreByRateId(rateId);
        QueryUtil.checkUpdate(update, () -> new NotFoundException("Specialist not found."));
    }

    @Override
    @Transactional
    public void changePassword(String username, String oldPassword, String newPassword) {
        Specialist specialist = repository.findSpecialistByUsername(username)
                .orElseThrow(() -> new NotFoundException("Specialist not found."));
        if (!specialist.getPassword().equals(oldPassword))
            throw new CustomIllegalArgumentException("Old password is not incorrect.");
        specialist.setPassword(newPassword);
        super.update(specialist);
    }

    @Override
    public boolean isExistUsername(String username) {
        return repository.findSpecialistByUsername(username).isPresent();
    }

    @Override
    public boolean isExistEmail(String email) {
        return repository.findSpecialistByEmail(email).isPresent();
    }

    @Override
    public void checkStatusVerified(Long id) {
        Specialist specialist = findById(id).orElseThrow(() -> new NotFoundException("Specialist not found."));
        checkStatusVerified(specialist.getStatus());
    }
    private void checkStatusVerified(SpecialistStatus status) throws CustomIllegalArgumentException {
        if (status == SpecialistStatus.NEW)
            throw new CustomIllegalArgumentException("Specialist not yet confirmed");
        if (status == SpecialistStatus.SUSPENDED)
            throw new CustomIllegalArgumentException("Specialist suspended");
    }
}
