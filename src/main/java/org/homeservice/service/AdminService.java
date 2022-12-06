package org.homeservice.service;

import org.homeservice.entity.Admin;
import org.homeservice.entity.Service;
import org.homeservice.entity.SubService;
import org.homeservice.service.base.BaseService;

public interface AdminService extends BaseService<Admin,Long> {
    Service addService(String name, SubService... subServices);
}
