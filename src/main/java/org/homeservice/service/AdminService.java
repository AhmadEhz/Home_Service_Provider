package org.homeservice.service;

import org.homeservice.entity.Admin;
import org.homeservice.service.base.BaseService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface AdminService extends BaseService<Admin, Long> {
    void changePassword(Admin admin, String oldPassword, String newPassword);

    boolean isExistsByUsername(String username);

    Optional<Admin> loadByUsername(String username);
}
