package org.homeservice.service.base;

import java.util.List;
import java.util.Optional;

public interface BaseService<T, ID> {
    void save(T t);

    Optional<T> loadById(ID id);

    List<T> loadAll();

    void update(T t);

    void delete(T t);
}
