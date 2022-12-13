package org.homeservice.service;

import org.homeservice.entity.SubService;
import org.homeservice.service.base.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SubServiceService extends BaseService<SubService,Long> {
    List<SubService> findAllBySpecialist(Long specialistId);
}
