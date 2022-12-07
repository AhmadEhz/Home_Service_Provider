package org.homeservice.repository.base;

import java.util.List;
import java.util.Optional;

public interface BaseRepository<E,ID> {
    public Optional<E> findById(ID id);
    public void save(E e);
    public void update(E e);
    public void delete(E e);
    public List<E> findAll();
}
