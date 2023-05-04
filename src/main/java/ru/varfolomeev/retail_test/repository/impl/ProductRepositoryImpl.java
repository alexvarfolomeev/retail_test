package ru.varfolomeev.retail_test.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.varfolomeev.retail_test.model.Product;
import ru.varfolomeev.retail_test.repository.ProductRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    public static final String SELECT_PRODUCT =
            "SELECT p.product_id AS product_id, p.product_code AS product_code, p.product_desc AS product_desc, p.product_category_code AS category," +
                    " p.product_category_name AS product_category_name FROM products AS p" +
                    " WHERE product_id = ?";
    private static final String SELECT_ALL_PRODUCTS =
            "SELECT product_id, product_code, product_desc, product_category_code, product_category_name FROM products";

    private static final String INSERT_PRODUCT =
            "INSERT INTO products (product_code, product_desc, product_category_code, product_category_name) "
                    + "VALUES (?, ?, ?, ?)";
    private static final String UPDATE_PRODUCT =
            "UPDATE products SET product_code = ?, product_desc = ?, product_category_code = ?, product_category_name = ?"
                    + " WHERE product_id = ?";
    private static final String DELETE_PRODUCT = "DELETE FROM products WHERE product_id = ?";

    private static final String SELECT_PRODUCT_BY_CODE = "SELECT product_id, product_code, product_desc, product_category_code, product_category_name FROM products WHERE product_code = ?";


    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Product> findByCode(Integer code) {
        return jdbcTemplate.query(SELECT_PRODUCT_BY_CODE, this::mapRowToProduct, code).stream().findAny();
    }

    @Override
    public Optional<Product> findById(Long id) {
        return jdbcTemplate.query(SELECT_PRODUCT, this::mapRowToProduct, id).stream().findAny();
    }

    @Override
    public Collection<Product> findAll() {
        return jdbcTemplate.query(SELECT_ALL_PRODUCTS, this::mapRowToProduct);
    }

    @Override
    public void save(Product product) {
        jdbcTemplate.update(INSERT_PRODUCT, product.getCode(), product.getDescription(), product.getCategoryCode(), product.getCategoryName());
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update(DELETE_PRODUCT, id);
    }

    @Override
    public int update(Product product) {
        if (findById(product.getId()).isEmpty()){
            throw new RuntimeException(String.format("Product with id - %d - doesn`t exist", product.getId()));
        }
        jdbcTemplate.update(UPDATE_PRODUCT, product.getCode(), product.getDescription(), product.getCategoryCode(), product.getCategoryName());
        return 1;
    }

    private Product mapRowToProduct(ResultSet resultSet, int i) throws SQLException {
        long productId = resultSet.getLong("product_id");
        return new Product(
                productId,
                resultSet.getLong("product_code"),
                resultSet.getString("product_desc"),
                resultSet.getString("category"),
                resultSet.getString("product_category_name")
        );
    }
}
