package ru.varfolomeev.retail_test.repository;

import org.springframework.stereotype.Repository;
import ru.varfolomeev.retail_test.model.Product;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface ProductRepository extends BasicCrudRepository<Product>{
    Optional<Product> findByCode(Integer code);
    Optional<Product> findById(Long id);
    Collection<Product> findAll();
    void save(Product product);
    void delete(Long id);
    int update(Product product);


}
