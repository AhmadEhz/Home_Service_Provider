package org.homeservice.service;

import org.homeservice.entity.SubService;
import org.homeservice.service.base.BaseService;

import java.util.List;

public interface SubServiceService extends BaseService<SubService, Long> {
    List<SubService> findAll(Long serviceId);

    void editDescription(String newDescription, Long id);

    void editBasePrice(double price, Long id);
}
