package org.homeservice.repository.hibernate;

import org.homeservice.entity.Service;
import org.homeservice.repository.hibernate.base.HibernateBaseRepository;

import java.util.Optional;

public interface HibernateServiceRepository extends HibernateBaseRepository<Service,Long> {
    Optional<Service> findByName(String name);
}
