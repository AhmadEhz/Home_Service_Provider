package org.homeservice.service.impl;

import org.homeservice.entity.Admin;
import org.homeservice.repository.AdminRepository;
import org.homeservice.service.AdminService;
import org.homeservice.service.PersonService;
import org.homeservice.service.base.BaseServiceImpl;
import org.homeservice.util.exception.CustomIllegalArgumentException;
import org.homeservice.util.exception.NonUniqueException;
import org.homeservice.util.exception.NotFoundException;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Scope("singleton")
public class AdminServiceImpl extends BaseServiceImpl<Admin, Long, AdminRepository> implements AdminService {
    private final PasswordEncoder passwordEncoder;
    private final PersonService personService;

    public AdminServiceImpl(AdminRepository repository, PasswordEncoder passwordEncoder, PersonService personService) {
        super(repository);
        this.passwordEncoder = passwordEncoder;
        this.personService = personService;
    }

    @Override
    @Transactional
    public void save(Admin admin) {
        if (isExistsByUsername(admin.getUsername()))
            throw new NonUniqueException("Username is exist.");
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        super.save(admin);
    }

    @Override
    public void changePassword(String username, String oldPassword, String newPassword) {
        Admin admin = repository.findAdminByUsername(username).orElseThrow
                (() -> new NotFoundException("Admin not found."));
        if (!passwordEncoder.matches(oldPassword, admin.getPassword()))
            throw new CustomIllegalArgumentException("Old password is incorrect");
        admin.setPassword(passwordEncoder.encode(newPassword));
        update(admin);
    }

    @Override
    public Optional<Admin> loadByUsername(String username) {
        return repository.findAdminByUsername(username);
    }

    @Override
    public boolean isExistsByUsername(String username) {
        return personService.isExistsByUsername(username);
    }
}
