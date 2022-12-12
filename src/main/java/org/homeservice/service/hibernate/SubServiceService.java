package org.homeservice.service.hibernate;

import org.homeservice.entity.SubService;
import org.homeservice.service.hibernate.base.BaseService;

import java.util.List;

public interface SubServiceService extends BaseService<SubService, Long> {
    List<SubService> loadAll(Long serviceId);

    void editDescription(String newDescription, Long id);

    void editBasePrice(double price, Long id);
}
