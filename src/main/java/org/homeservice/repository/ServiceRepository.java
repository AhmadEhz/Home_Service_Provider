package org.homeservice.repository;

import org.homeservice.entity.Service;
import org.homeservice.repository.base.BaseRepository;

import java.util.Optional;

public interface ServiceRepository  extends BaseRepository<Service,Long> {
    Optional<Service> findByName(String name);
}
