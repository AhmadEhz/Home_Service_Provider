package org.homeservice.service.base;

import jakarta.persistence.EntityManager;
import jakarta.persistence.RollbackException;
import jakarta.validation.ConstraintViolation;
import org.homeservice.repository.base.BaseRepository;
import org.homeservice.util.HibernateUtil;
import org.homeservice.util.exception.CustomIllegalArgumentException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class BaseServiceImpl<E, ID, R extends BaseRepository<E, ID>> implements BaseService<E, ID> {
    protected final R repository;

    protected BaseServiceImpl(R repository) {
        this.repository = repository;
    }

    @Override
    public Optional<E> loadById(ID id) {
        return repository.findById(id);
    }

    @Override
    public List<E> loadAll() {
        return repository.findAll();
    }

    @Override
    public void save(E e) {
        checkEntity(e);
        executeUpdate(() -> repository.save(e));
    }

    @Override
    public void update(E e) {
        checkEntity(e);
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
                re.addSuppressed(e);
                throw re;
            }

            throw e;
        }
        em.close();
    }

    protected <T> void checkEntity(T t) {
        Set<ConstraintViolation<T>> validate = HibernateUtil.getValidator().validate(t);
        if (!validate.isEmpty())
            throw new CustomIllegalArgumentException(validate.toString());
    }
}
