package org.homeservice.repository.hibernate.impl;

import org.homeservice.entity.SubService;
import org.homeservice.repository.hibernate.HibernateSubServiceRepository;
import org.homeservice.repository.hibernate.base.HibernateBaseRepositoryImpl;
import org.homeservice.util.HibernateUtil;

import java.util.List;
import java.util.Optional;

public class HibernateSubServiceRepositoryImpl extends HibernateBaseRepositoryImpl<SubService, Long> implements HibernateSubServiceRepository {

    private static HibernateSubServiceRepository repository;

    private HibernateSubServiceRepositoryImpl() {
    }

    @Override
    protected Class<SubService> getEntityClass() {
        return SubService.class;
    }


    @Override
    public int updateDescription(String newDescription, Long id) {
        String query = "update SubService set description =:desc where id =:id";
        return HibernateUtil.getCurrentEntityManager().createQuery(query)
                .setParameter("desc", newDescription).setParameter("id", id).executeUpdate();
    }

    @Override
    public int updateBasePrice(double basePrice, Long id) {
        String query = "update SubService set basePrice =:desc where id =:id";
        return HibernateUtil.getCurrentEntityManager().createQuery(query)
                .setParameter("price", basePrice).setParameter("id", id).executeUpdate();
    }

    @Override
    public Optional<SubService> findByName(String name) {
        String query = "select s from SubService as s where s.name =:name";
        return Optional.ofNullable(HibernateUtil.getCurrentEntityManager().createQuery(query, SubService.class)
                .setParameter("name", name).getSingleResult());
    }

    @Override
    public List<SubService> findAll(Long serviceId) {
        String query = "select s from SubService as s where s.service.id = :servId";
        return HibernateUtil.getCurrentEntityManager().createQuery(query, SubService.class)
                .setParameter("servId", serviceId).getResultList();
    }

    public static HibernateSubServiceRepository getRepository() {
        if (repository == null)
            repository = new HibernateSubServiceRepositoryImpl();
        return repository;
    }
}
