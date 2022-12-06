package org.homeservice.repository.base;

import org.homeservice.util.HibernateUtil;

import java.util.List;

public abstract class BaseRepositoryImpl<E, ID> implements BaseRepository<E, ID> {

    protected BaseRepositoryImpl() {
    }

    @Override
    public E readById(ID id) {
        return HibernateUtil.getCurrentEntityManager().
                find(getEntityClass(), id);
    }

    @Override
    public void save(E e) {
        HibernateUtil.getCurrentEntityManager().persist(e);
    }

    @Override
    public void update(E e) {
        HibernateUtil.getCurrentEntityManager().merge(e);
    }

    @Override
    public void delete(E e) {
        HibernateUtil.getCurrentEntityManager().remove(e);
    }

    @Override
    public List<E> findAll() {
        String query = "select e from " + getEntityClass().getSimpleName() + " as e";
        return HibernateUtil.getCurrentEntityManager()
                .createQuery(query, getEntityClass()).getResultList();
    }

    protected abstract Class<E> getEntityClass();
}
