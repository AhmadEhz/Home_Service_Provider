package org.homeservice.repository.hibernate.impl;

import org.homeservice.entity.SubServiceSpecialist;
import org.homeservice.entity.id.SubServiceSpecialistId;
import org.homeservice.repository.hibernate.HibernateSubServiceSpecialistRepository;
import org.homeservice.repository.hibernate.base.HibernateBaseRepositoryImpl;

public class HibernateSubServiceSpecialistRepositoryImpl extends HibernateBaseRepositoryImpl<SubServiceSpecialist, SubServiceSpecialistId>
        implements HibernateSubServiceSpecialistRepository {
    private static HibernateSubServiceSpecialistRepository repository;

    private HibernateSubServiceSpecialistRepositoryImpl() {
    }

    @Override
    protected Class<SubServiceSpecialist> getEntityClass() {
        return SubServiceSpecialist.class;
    }

    public static HibernateSubServiceSpecialistRepository getRepository() {
        if (repository == null)
            repository = new HibernateSubServiceSpecialistRepositoryImpl();
        return repository;
    }
}