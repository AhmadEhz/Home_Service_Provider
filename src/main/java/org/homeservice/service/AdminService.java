package org.homeservice.service;

import org.homeservice.entity.Admin;
import org.homeservice.service.base.BaseService;
import org.springframework.stereotype.Service;

@Service
public interface AdminService extends BaseService<Admin, Long> {
    void changePassword(String username, String oldPassword, String newPassword);
}
