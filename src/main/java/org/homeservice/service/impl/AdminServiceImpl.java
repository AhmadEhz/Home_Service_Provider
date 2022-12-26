package org.homeservice.service.impl;

import org.homeservice.entity.Admin;
import org.homeservice.repository.AdminRepository;
import org.homeservice.service.AdminService;
import org.homeservice.service.base.BaseServiceImpl;
import org.homeservice.util.exception.CustomIllegalArgumentException;
import org.homeservice.util.exception.NonUniqueException;
import org.homeservice.util.exception.NotFoundException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Scope("singleton")
public class AdminServiceImpl extends BaseServiceImpl<Admin, Long, AdminRepository> implements AdminService {
    public AdminServiceImpl(AdminRepository repository) {
        super(repository);
    }

    @Override
    @Transactional
    public void save(Admin admin) {
        if (isExistsByUsername(admin.getUsername()))
            throw new NonUniqueException("Username is exist.");
        super.save(admin);
    }

    @Override
    public void changePassword(String username, String oldPassword, String newPassword) {
        Admin admin = repository.findAdminByUsername(username).orElseThrow
                (() -> new NotFoundException("Admin not found."));
        if (!admin.getPassword().equals(oldPassword))
            throw new CustomIllegalArgumentException("Old password is incorrect");
        admin.setPassword(newPassword);
        update(admin);
    }

    @Override
    public Optional<Admin> loadByUsername(String username) {
        return repository.findAdminByUsername(username);
    }

    @Override
    public boolean isExistsByUsername(String username) {
        return repository.findAdminByUsername(username).isPresent();
    }
}
