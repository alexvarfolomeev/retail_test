package ru.varfolomeev.retail_test.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.varfolomeev.retail_test.model.Customer;
import ru.varfolomeev.retail_test.repository.CustomerRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CustomerRepositoryImpl implements CustomerRepository {

    public static final String SELECT_CUSTOMER =
            "SELECT customer_id, customer_code, address, chain_name FROM customers WHERE customer_id = ?";
    private static final String SELECT_ALL_CUSTOMERS =
            "SELECT customer_id, customer_code, address, chain_name FROM customers";

    private static final String SELECT_CUSTOMER_BY_CODE =
            "SELECT customer_id, customer_code, address, chain_name FROM customers WHERE customer_code = ?";

    private static final String INSERT_CUSTOMER =
            "INSERT INTO customers (customer_code, address, chain_name) "
                    + "VALUES (?, ?, ?)";
    private static final String UPDATE_CUSTOMER =
            "UPDATE customers SET customer_code = ?, address = ?, chain_name = ?"
                    + " WHERE customer_id = ?";
    private static final String DELETE_CUSTOMER = "DELETE FROM customers WHERE customer_id = ?";


    private final JdbcTemplate jdbcTemplate;

    private final ChainRepositoryImpl chainRepository;

    @Override
    public Optional<Customer> findById(Long id) {
        return jdbcTemplate.query(SELECT_CUSTOMER, this::mapRowToCustomer, id).stream().findAny();
    }

    public Optional<Customer> findByCode(Integer code) {
        return jdbcTemplate.query(SELECT_CUSTOMER, this::mapRowToCustomer, code).stream().findAny();
    }

    @Override
    public Collection<Customer> findAll() {
        return jdbcTemplate.query(SELECT_ALL_CUSTOMERS, this::mapRowToCustomer);
    }

    @Override
    public void save(Customer obj) {
        jdbcTemplate.update(INSERT_CUSTOMER, obj.getName(), obj.getCode(), obj.getChain().getId());
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update(DELETE_CUSTOMER, id);
    }

    @Override
    public int update(Customer obj) {
        if (findById(obj.getId()).isEmpty()) {
            throw new RuntimeException(String.format("Customer with id - %d - doesn`t exist", obj.getId()));
        }
        jdbcTemplate.update(UPDATE_CUSTOMER, obj.getName(), obj.getCode(), obj.getChain());
        return 1;
    }

    private Customer mapRowToCustomer(ResultSet resultSet, int i) throws SQLException {
        long customer_id = resultSet.getLong("customer_id");
        return new Customer(
                customer_id,
                resultSet.getInt("code"),
                resultSet.getString("name"),
                chainRepository.findByName(resultSet.getString("chain_name")).orElseThrow()
        );
    }
}
