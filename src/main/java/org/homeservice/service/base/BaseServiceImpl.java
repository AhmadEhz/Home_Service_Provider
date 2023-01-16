package org.homeservice.service.base;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.homeservice.util.exception.CustomIllegalArgumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class BaseServiceImpl<T, ID, R extends JpaRepository<T, ID>> implements BaseService<T, ID> {
    protected R repository;
    @Autowired
    protected Validator validator;

    protected BaseServiceImpl(R repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public void save(T t) {
        validate(t);
        repository.save(t);
    }

    @Override
    public Optional<T> loadById(ID id) {
        return repository.findById(id);
    }

    @Override
    public List<T> loadAll() {
        return repository.findAll();
    }

    @Override
    @Transactional
    public void update(T t) {
        validate(t);
        repository.save(t);
    }

    @Override
    @Transactional
    public void delete(T t) {
        repository.delete(t);
    }

    protected void validate(T t) throws CustomIllegalArgumentException {
        Set<ConstraintViolation<T>> violations = validator.validate(t);
        if(!violations.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            violations.forEach(violation -> stringBuilder.append(violation.getMessage()).append('\n'));
            throw new CustomIllegalArgumentException(stringBuilder.toString());
        }
    }
}
