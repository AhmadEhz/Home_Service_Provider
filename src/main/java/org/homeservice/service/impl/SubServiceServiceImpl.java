package org.homeservice.service.impl;

import org.homeservice.entity.SubService;
import org.homeservice.repository.SubServiceRepository;
import org.homeservice.service.SubServiceService;
import org.homeservice.service.base.BaseServiceImpl;
import org.homeservice.util.exception.NotFoundException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Scope("singleton")
public class SubServiceServiceImpl extends BaseServiceImpl<SubService, Long, SubServiceRepository>
        implements SubServiceService {

    public SubServiceServiceImpl(SubServiceRepository repository) {
        super(repository);
    }

    @Override
    public List<SubService> loadAllBySpecialist(Long specialistId) {
        return repository.findAllBySpecialist(specialistId);
    }

    @Override
    public void editDescription(String newDescription, Long id) {
        SubService subService = loadById(id);
        subService.setDescription(newDescription);
        update(subService);
    }

    @Override
    public void editBasePrice(double basePrice, Long id) {
        SubService subService = loadById(id);
        subService.setBasePrice(basePrice);
        update(subService);
    }

    private SubService loadById(Long id) throws NotFoundException {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("SubService not found."));
    }

}
