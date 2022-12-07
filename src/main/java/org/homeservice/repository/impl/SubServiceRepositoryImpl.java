package org.homeservice.repository.impl;

import org.homeservice.entity.SubService;
import org.homeservice.repository.SubServiceRepository;
import org.homeservice.repository.base.BaseRepositoryImpl;
import org.homeservice.util.HibernateUtil;

public class SubServiceRepositoryImpl extends BaseRepositoryImpl<SubService, Long> implements SubServiceRepository {

    private static SubServiceRepository repository;

    private SubServiceRepositoryImpl() {
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

    public static SubServiceRepository getRepository() {
        if (repository == null)
            repository = new SubServiceRepositoryImpl();
        return repository;
    }
}
