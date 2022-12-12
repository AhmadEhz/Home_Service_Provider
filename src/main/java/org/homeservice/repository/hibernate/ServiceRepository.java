package org.homeservice.repository.hibernate;

import org.homeservice.entity.Service;
import org.homeservice.repository.hibernate.base.BaseRepository;

import java.util.Optional;

public interface ServiceRepository  extends BaseRepository<Service,Long> {
    Optional<Service> findByName(String name);
}
