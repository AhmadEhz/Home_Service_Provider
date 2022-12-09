package org.homeservice.repository.impl;

import org.homeservice.entity.Specialist;
import org.homeservice.entity.SpecialistStatus;
import org.homeservice.repository.SpecialistRepository;
import org.homeservice.repository.base.BaseRepositoryImpl;
import org.homeservice.util.HibernateUtil;
import org.homeservice.util.QueryUtil;

import java.util.List;
import java.util.Optional;

public class SpecialistRepositoryImpl extends BaseRepositoryImpl<Specialist, Long> implements SpecialistRepository {
    private static SpecialistRepository repository;

    private SpecialistRepositoryImpl() {
    }

    @Override
    public List<Specialist> findAll(SpecialistStatus status) {
        String query = "select s from Specialist as s where s.status=:status";
        return HibernateUtil.getCurrentEntityManager().createQuery(query, Specialist.class)
                .setParameter("status", status).getResultList();
    }

    @Override
    public int changeStatus(Long specialistId, SpecialistStatus status) {
        String query = "update Specialist set status = :status where id = :id";
        return HibernateUtil.getCurrentEntityManager().createQuery(query)
                .setParameter("status", status).setParameter("id", specialistId).executeUpdate();
    }

    @Override
    public int updateScore(Long id) {
        String query = """
                update Specialist set score =
                (select avg(r.score) from Rate as r where r.order.specialist.id=:id)
                where id = :id""";
        return HibernateUtil.getCurrentEntityManager()
                .createQuery(query).setParameter("id", id).executeUpdate();
    }

    @Override
    public Optional<Specialist> findByUsername(String username) {
        String query = "select s from Specialist as s where s.username = :username";
        return Optional.ofNullable(
                QueryUtil.getSingleResult(HibernateUtil.getCurrentEntityManager()
                        .createQuery(query, Specialist.class).setParameter("username", username)));
    }

    @Override
    public Optional<Specialist> findByEmail(String email) {
        String query = "select s from Specialist as s where s.email = :email";
        return Optional.ofNullable(
                QueryUtil.getSingleResult(HibernateUtil.getCurrentEntityManager()
                        .createQuery(query, Specialist.class).setParameter("username", email)));
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
