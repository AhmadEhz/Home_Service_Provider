package org.homeservice.service.hibernate.base;

import java.util.List;
import java.util.Optional;

public interface BaseService<E, ID> {
    Optional<E> loadById(ID id);

    List<E> loadAll();

    void save(E e);

    void update(E e);

    void remove(E e);
}
