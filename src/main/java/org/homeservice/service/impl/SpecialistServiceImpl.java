package org.homeservice.service.impl;

import jakarta.validation.Valid;
import org.homeservice.entity.Specialist;
import org.homeservice.entity.SpecialistStatus;
import org.homeservice.repository.SpecialistRepository;
import org.homeservice.service.*;
import org.homeservice.service.base.BaseServiceImpl;
import org.homeservice.util.*;
import org.homeservice.util.exception.*;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;

@Service
@Scope("singleton")
public class SpecialistServiceImpl extends BaseServiceImpl<Specialist, Long, SpecialistRepository>
        implements SpecialistService {
    private final PersonService personService;
    private final Specifications specifications;
    private final PasswordEncoder passwordEncoder;

    public SpecialistServiceImpl(SpecialistRepository repository, Specifications specifications,
                                 PasswordEncoder passwordEncoder, PersonService personService) {
        super(repository);
        this.specifications = specifications;
        this.passwordEncoder = passwordEncoder;
        this.personService = personService;
    }

    @Override
    @Transactional
    public void save(Specialist specialist) {
        validate(specialist);
        if (isExistUsername(specialist.getUsername()))
            throw new NonUniqueException("Username is not unique.");
        if (isExistEmail(specialist.getEmail()))
            throw new NonUniqueException("Email is not unique.");
        specialist.setPassword(passwordEncoder.encode(specialist.getPassword()));
        super.save(specialist);
    }

    @Override
    public void addAvatar(Long id, MultipartFile avatar) {
        if (avatar == null)
            throw new CustomIllegalArgumentException("Avatar is empty.");
        Specialist specialist = findById(id).orElseThrow(() -> new NotFoundException("Specialist not found."));
        if (avatar.getSize() > Values.MAX_AVATAR_SIZE) //avatar size > 300KB
            throw new CustomIllegalArgumentException
                    ("Image size is bigger than " + (Values.MAX_AVATAR_SIZE / 1024) + " KB");

        if (!avatar.getOriginalFilename().toLowerCase().endsWith(Values.AVATAR_FORMAT))
            throw new CustomIllegalArgumentException("Image format must be " + Values.AVATAR_FORMAT);

        if (!specialist.setAvatar(avatar))
            throw new CustomIllegalArgumentException("File is corrupted");
        update(specialist);
    }

    @Override
    public void addAvatar(Long id, File avatar) {
        Specialist specialist = findById(id).orElseThrow(() -> new NotFoundException("Specialist not found."));
        if (avatar.length() > Values.MAX_AVATAR_SIZE)
            throw new CustomIllegalArgumentException
                    ("Image size is bigger than " + (Values.MAX_AVATAR_SIZE / 1024) + " KB");
        if (!avatar.getName().toLowerCase().endsWith(Values.AVATAR_FORMAT))
            throw new CustomIllegalArgumentException("Image format must be " + Values.AVATAR_FORMAT);

        if (!specialist.setAvatar(avatar))
            throw new CustomIllegalArgumentException("File is corrupted");
        update(specialist);
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
    public List<Specialist> loadAllByFilter(Map<String, String> filters) {
        return repository.findAll(specifications.getSpecialist(filters));
    }

    @Override
    public List<Specialist> loadAllBySubService(Long subServiceId) {
        return repository.findAllBySubService(subServiceId);
    }

    @Override
    @Transactional
    public void verifySpecialist(Long id) {
        changeStatus(id, SpecialistStatus.ACCEPTED);
    }

    @Override
    @Transactional
    public void suspendSpecialist(Long id) {
        changeStatus(id, SpecialistStatus.SUSPENDED);
    }

    @Override
    @Transactional
    public void changeStatus(Long id, SpecialistStatus status) {
        int update = repository.updateStatus(id, status);
        QueryUtil.checkUpdate(update, () -> new NotFoundException("Specialist not found."));
    }

    @Override
    @Transactional
    public void updateScore(Long id) {
        repository.updateScore(id);
        repository.suspendSpecialistIfScoreIsNegative(id);
    }

    @Override
    @Transactional
    public void updateScoreByRateId(Long rateId) {
        repository.updateScoreByRateId(rateId);
        repository.suspendSpecialistIfScoreIsNegativeByRate(rateId);
    }

    @Override
    @Transactional
    public void setVerificationId(Long id, Long verificationId) {
        repository.setVerificationCodeId(id, verificationId);
    }

    @Override
    @Transactional
    public void changePassword(String username, String oldPassword, String newPassword) {
        Specialist specialist = repository.findSpecialistByUsername(username)
                .orElseThrow(() -> new NotFoundException("Specialist not found."));
        if (!passwordEncoder.matches(oldPassword, specialist.getPassword()))
            throw new CustomIllegalArgumentException("Old password is not incorrect.");
        specialist.setPassword(passwordEncoder.encode(newPassword));
        super.update(specialist);
    }


    @Override
    public boolean isExistUsername(String username) {
        return personService.isExistsByUsername(username);
    }

    @Override
    public boolean isExistEmail(String email) {
        return repository.findSpecialistByEmail(email).isPresent();
    }

    @Override
    public void delete(Specialist specialist) {
        Specialist loadSpecialist = findById(specialist.getId()).orElseThrow(() ->
                new NotFoundException("Specialist not found."));
        if (!checkBeforeDelete(specialist, loadSpecialist, false))
            throw new CustomIllegalArgumentException("Specialist properties is incorrect.");
        super.delete(specialist);
    }

    @Override
    public void deleteByAdmin(Specialist specialist) {
        Specialist loadSpecialist = findById(specialist.getId()).orElseThrow(() ->
                new NotFoundException("Specialist not found."));
        if (!checkBeforeDelete(specialist, loadSpecialist, true))
            throw new CustomIllegalArgumentException("Specialist properties is incorrect.");
        super.delete(specialist);
    }

    private boolean checkBeforeDelete(Specialist specialist, Specialist loadSpecialist, boolean deleteByAdmin) {
        if (specialist.getFirstName().equals(loadSpecialist.getFirstName()) &&
            specialist.getLastName().equals(loadSpecialist.getLastName()) &&
            specialist.getEmail().equals(loadSpecialist.getEmail()) &&
            specialist.getUsername().equals(loadSpecialist.getUsername()))
            return specialist.getPassword().equals(loadSpecialist.getPassword()) || deleteByAdmin;
        return false;
    }
}
