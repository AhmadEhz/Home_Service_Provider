package org.homeservice.service.impl;

import org.homeservice.entity.Admin;
import org.homeservice.repository.AdminRepository;
import org.homeservice.service.AdminService;
import org.homeservice.service.base.BaseServiceImpl;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
public class AdminServiceImpl extends BaseServiceImpl<Admin, Long, AdminRepository> implements AdminService {
    public AdminServiceImpl(AdminRepository repository) {
        super(repository);
    }
}
