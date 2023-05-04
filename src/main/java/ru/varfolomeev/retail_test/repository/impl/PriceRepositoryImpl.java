package ru.varfolomeev.retail_test.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.varfolomeev.retail_test.model.Price;
import ru.varfolomeev.retail_test.repository.PriceRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PriceRepositoryImpl implements PriceRepository {

    public static final String SELECT_PRICE =
            "SELECT p.price_id AS price_id, p.regular_price AS regular_price, pr.product_id AS product_id, c.chain_name AS chain_name, p.chain_id AS chain_id" +
                    " FROM prices AS p"
                    + " INNER JOIN products AS pr ON p.product_id = pr.product_id"
                    + " INNER JOIN chains AS c ON p.chain_id = c.chain_id"
                    + " WHERE price_id = ?";
    private static final String SELECT_ALL_PRICES =
            "SELECT p.price_id AS price_id, p.regular_price AS regular_price, pr.product_id AS product_id, pr.product_code AS product_code, p.chain_id AS chain_id" +
                    " FROM prices AS p"
                    + " JOIN products AS pr ON p.product_id = pr.product_id";

    private static final String INSERT_PRICE =
            "INSERT INTO prices (product_id, regular_price, chain_id) "
                    + "VALUES (?, ?, ?)";
    private static final String UPDATE_PRICE =
            "UPDATE prices SET product_id = ?, regular_price = ?, chain_id = ?"
                    + " WHERE price_id = ?";
    private static final String DELETE_PRICE = "DELETE FROM prices WHERE price_id = ?";


    private final JdbcTemplate jdbcTemplate;

    private final ChainRepositoryImpl chainRepository;

    private final ProductRepositoryImpl productRepository;

    @Override
    public Optional<Price> findById(Long id) {
        return jdbcTemplate.query(SELECT_PRICE, this::mapRowToPrice, id).stream().findAny();
    }

    @Override
    public Collection<Price> findAll() {
        return jdbcTemplate.query(SELECT_ALL_PRICES, this::mapRowToPrice);
    }

    @Override
    public void save(Price price) {
        jdbcTemplate.update(INSERT_PRICE, price.getProduct().getId(), price.getRegularPrice(), price.getChain().getId());
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update(DELETE_PRICE, id);
    }

    @Override
    public int update(Price price) {
        return jdbcTemplate.update(UPDATE_PRICE, price.getProduct().getId(), price.getRegularPrice(), price.getChain().getId(), price.getId());
    }

    private Price mapRowToPrice(ResultSet resultSet, int i) throws SQLException {
        long priceId = resultSet.getLong("price_id");
        return new Price(
                priceId,
                productRepository.findById(resultSet.getLong("product_id")).orElseThrow(),
                resultSet.getBigDecimal("regular_price"),
                chainRepository.findById(resultSet.getLong("chain_id")).orElseThrow()
        );
    }
}
