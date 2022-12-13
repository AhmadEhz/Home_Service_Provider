package org.homeservice.service.impl;

import org.homeservice.entity.Specialist;
import org.homeservice.repository.SpecialistRepository;
import org.homeservice.service.SpecialistService;
import org.homeservice.service.base.BaseServiceImpl;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
public class SpecialistServiceImpl extends BaseServiceImpl<Specialist, Long, SpecialistRepository>
        implements SpecialistService {
    public SpecialistServiceImpl(SpecialistRepository repository) {
        super(repository);
    }
}
