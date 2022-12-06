package org.homeservice.repository.base;

import java.util.List;

public interface BaseRepository<E,ID> {
    public E readById(ID id);
    public void save(E e);
    public void update(E e);
    public void delete(E e);
    public List<E> findAll();
}
