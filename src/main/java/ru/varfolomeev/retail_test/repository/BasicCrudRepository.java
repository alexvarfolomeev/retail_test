package ru.varfolomeev.retail_test.repository;

import java.util.Collection;
import java.util.Optional;

public interface BasicCrudRepository<T> {
    Optional<T> findById(Long id);
    Collection<T> findAll();
    void save(T obj);
    void delete(Long id);
    int update(T obj);
}
