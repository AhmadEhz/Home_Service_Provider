package org.homeservice.service.base;

import java.util.Optional;

public interface BaseService<E, ID> {
    Optional<E> loadById(ID id);

    void save(E e);

    void update(E e);

    void remove(E e);
}
