package org.homeservice.repository.hibernate.base;

import java.util.List;
import java.util.Optional;

public interface HibernateBaseRepository<E,ID> {
    public Optional<E> findById(ID id);
    public void save(E e);
    public void update(E e);
    public void delete(E e);
    public List<E> findAll();
}
