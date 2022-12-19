package org.homeservice.repository.hibernate;

import org.homeservice.entity.SubService;
import org.homeservice.repository.hibernate.base.HibernateBaseRepository;

import java.util.List;
import java.util.Optional;

public interface HibernateSubServiceRepository extends HibernateBaseRepository<SubService,Long> {

    int updateDescription(String newDescription, Long id);

    int updateBasePrice(double basePrice, Long id);

    Optional<SubService> findByName(String name);

    List<SubService> findAll(Long serviceId);
}
