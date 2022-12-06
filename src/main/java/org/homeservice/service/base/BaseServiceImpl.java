package org.homeservice.service.base;

import jakarta.persistence.EntityManager;
import jakarta.persistence.RollbackException;
import org.homeservice.repository.base.BaseRepository;
import org.homeservice.util.HibernateUtil;

public class BaseServiceImpl<E, ID, R extends BaseRepository<E, ID>> implements BaseService<E, ID> {
    private final R repository;
    protected BaseServiceImpl(R repository) {
        this.repository = repository;
    }

    @Override
    public E loadById(ID id) {
        return repository.readById(id);
    }

    @Override
    public void save(E e) {
        executeUpdate(() -> repository.save(e));
    }

    @Override
    public void update(E e) {
        executeUpdate(() -> repository.update(e));
    }

    @Override
    public void remove(E e) {
        executeUpdate(() -> repository.delete(e));
    }

    protected void executeUpdate(Runnable runnable) {
        EntityManager em = HibernateUtil.createEntityManager();
        try {
            em.getTransaction().begin();
            runnable.run();
            em.getTransaction().commit();
        } catch (Exception e) {
            try {
                em.getTransaction().rollback();
            } catch (RollbackException re) {
                e.printStackTrace();
            }
            e.printStackTrace();
        }
        em.close();
    }
}
