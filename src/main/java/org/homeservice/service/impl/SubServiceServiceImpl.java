package org.homeservice.service.impl;

import org.homeservice.entity.SubService;
import org.homeservice.repository.SubServiceRepository;
import org.homeservice.service.SubServiceService;
import org.homeservice.service.base.BaseServiceImpl;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Scope("singleton")
public class SubServiceServiceImpl extends BaseServiceImpl<SubService,Long,SubServiceRepository>
        implements SubServiceService {

    public SubServiceServiceImpl(SubServiceRepository repository) {
        super(repository);
    }

    @Override
    public List<SubService> findAllBySpecialist(Long specialistId) {
        return repository.findAllBySpecialist(specialistId);
    }


}
