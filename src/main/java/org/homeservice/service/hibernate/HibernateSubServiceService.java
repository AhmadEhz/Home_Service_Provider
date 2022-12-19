package org.homeservice.service.hibernate;

import org.homeservice.entity.SubService;
import org.homeservice.service.hibernate.base.HibernateBaseService;

import java.util.List;

public interface HibernateSubServiceService extends HibernateBaseService<SubService, Long> {
    List<SubService> loadAll(Long serviceId);

    void editDescription(String newDescription, Long id);

    void editBasePrice(double price, Long id);
}
