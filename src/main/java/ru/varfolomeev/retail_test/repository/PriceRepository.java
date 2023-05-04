package ru.varfolomeev.retail_test.repository;

import ru.varfolomeev.retail_test.model.Price;

import java.util.Collection;
import java.util.Optional;

public interface PriceRepository extends BasicCrudRepository<Price>{
    Optional<Price> findById(Long id);
    Collection<Price> findAll();
    void save(Price price);
    void delete(Long id);
    int update(Price price);
}
