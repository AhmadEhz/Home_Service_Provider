package org.homeservice.repository.impl;

import org.homeservice.entity.Specialist;
import org.homeservice.entity.SpecialistStatus;
import org.homeservice.repository.SpecialistRepository;
import org.homeservice.repository.base.BaseRepositoryImpl;
import org.homeservice.util.HibernateUtil;

import java.util.List;

public class SpecialistRepositoryImpl extends BaseRepositoryImpl<Specialist, Long> implements SpecialistRepository {
    private static SpecialistRepository repository;

    private SpecialistRepositoryImpl() {
    }

    @Override
    public List<Specialist> findAll(SpecialistStatus status) {
        String query = "select s from Specialist as s where s.status=:status";
        return HibernateUtil.getCurrentEntityManager().createQuery(query, Specialist.class)
                .setParameter("status",status).getResultList();
    }

    @Override
    public int changeStatus(Long specialistId, SpecialistStatus status) {
        String query = "update Specialist set status = :status where id = :id";
        return HibernateUtil.getCurrentEntityManager().createQuery(query)
                .setParameter("status", status).setParameter("id", specialistId).executeUpdate();
    }

    @Override
    protected Class<Specialist> getEntityClass() {
        return Specialist.class;
    }

    public static SpecialistRepository getRepository() {
        if (repository == null)
            repository = new SpecialistRepositoryImpl();
        return repository;
    }
}
