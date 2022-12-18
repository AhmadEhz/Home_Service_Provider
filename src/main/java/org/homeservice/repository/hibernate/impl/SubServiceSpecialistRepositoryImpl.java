package org.homeservice.repository.hibernate.impl;

import org.homeservice.entity.SubServiceSpecialist;
import org.homeservice.entity.id.SubServiceSpecialistId;
import org.homeservice.repository.SubServiceRepository;
import org.homeservice.repository.hibernate.SubServiceSpecialistRepository;
import org.homeservice.repository.hibernate.base.BaseRepositoryImpl;

public class SubServiceSpecialistRepositoryImpl extends BaseRepositoryImpl<SubServiceSpecialist, SubServiceSpecialistId>
        implements SubServiceSpecialistRepository {
    private static SubServiceSpecialistRepository repository;

    private SubServiceSpecialistRepositoryImpl() {
    }

    @Override
    protected Class<SubServiceSpecialist> getEntityClass() {
        return SubServiceSpecialist.class;
    }

    public static SubServiceSpecialistRepository getRepository() {
        if (repository == null)
            repository = new SubServiceSpecialistRepositoryImpl();
        return repository;
    }
}