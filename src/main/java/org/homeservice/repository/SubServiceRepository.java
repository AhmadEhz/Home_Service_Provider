package org.homeservice.repository;

import org.homeservice.entity.SubService;
import org.homeservice.repository.base.BaseRepository;

import java.util.List;
import java.util.Optional;

public interface SubServiceRepository extends BaseRepository<SubService,Long> {

    int updateDescription(String newDescription, Long id);

    int updateBasePrice(double basePrice, Long id);

    Optional<SubService> findByName(String name);

    List<SubService> findAll(Long serviceId);
}
