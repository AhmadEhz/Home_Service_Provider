package org.homeservice.service.base;

public interface BaseService<E, ID> {
    E loadById(ID id);

    void save(E e);

    void update(E e);

    void remove(E e);
}
