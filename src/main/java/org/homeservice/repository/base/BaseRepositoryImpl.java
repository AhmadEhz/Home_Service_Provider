package org.homeservice.repository.base;

import org.homeservice.util.HibernateUtil;

public abstract class BaseRepositoryImpl<E, ID> implements BaseRepository<E, ID> {

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

    protected abstract Class<E> getEntityClass();
}
