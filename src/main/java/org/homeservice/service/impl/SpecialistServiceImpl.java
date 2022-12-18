package org.homeservice.service.impl;

import lombok.NonNull;
import org.homeservice.entity.Specialist;
import org.homeservice.entity.SpecialistStatus;
import org.homeservice.repository.SpecialistRepository;
import org.homeservice.service.SpecialistService;
import org.homeservice.service.base.BaseServiceImpl;
import org.homeservice.util.exception.CustomIllegalArgumentException;
import org.homeservice.util.exception.NonUniqueException;
import org.homeservice.util.exception.NotFoundException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Scope("singleton")
public class SpecialistServiceImpl extends BaseServiceImpl<Specialist, Long, SpecialistRepository>
        implements SpecialistService {
    public SpecialistServiceImpl(SpecialistRepository repository) {
        super(repository);
    }

    @Override
    public void save(Specialist specialist) {
        if(isExistUsername(specialist.getUsername()))
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
    public void changeStatus(Long id, @NonNull SpecialistStatus status) {
        int update = repository.updateStatus(id, status);
        checkUpdate(update);
    }

    @Override
    public void updateScore(Long id) {
        int update = repository.updateScore(id);
        checkUpdate(update);
    }

    @Override
    public void updateScoreByRateId(Long rateId) {
        int update = repository.updateScoreByRateId(rateId);
        checkUpdate(update);
    }

    @Override
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

    private void checkUpdate(int update) {
        if (update < 1)
            throw new NotFoundException("Specialist not found.");
    }
}
