package org.homeservice.repository.impl;

import org.homeservice.entity.Specialist;
import org.homeservice.repository.SpecialistRepository;
import org.homeservice.repository.base.BaseRepositoryImpl;

public class SpecialistRepositoryImpl extends BaseRepositoryImpl<Specialist,Long> implements SpecialistRepository {
    private static SpecialistRepository repository;
    private SpecialistRepositoryImpl() {
    }
    @Override
    protected Class<Specialist> getEntityClass() {
        return Specialist.class;
    }
    public static SpecialistRepository getRepository() {
        if(repository==null)
            repository = new SpecialistRepositoryImpl();
        return repository;
    }
}
