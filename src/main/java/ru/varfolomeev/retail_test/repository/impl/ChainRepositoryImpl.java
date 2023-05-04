package ru.varfolomeev.retail_test.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.varfolomeev.retail_test.model.Chain;
import ru.varfolomeev.retail_test.repository.ChainRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ChainRepositoryImpl implements ChainRepository {

    public static final String SELECT_CHAIN =
            "SELECT chain_id, chain_name FROM chains WHERE chain_id = ?";
    private static final String SELECT_ALL_CHAINS =
            "SELECT chain_id, chain_name FROM chains";

    private static final String INSERT_CHAIN =
            "INSERT INTO chains (chain_name) "
                    + "VALUES (?)";
    private static final String UPDATE_CHAIN =
            "UPDATE chains SET chain_name = ?"
                    + " WHERE chain_id = ?";
    private static final String DELETE_CHAIN = "DELETE FROM chains WHERE chain_id = ?";

    private static final String SELECT_CHAIN_BY_NAME = "SELECT chain_id, chain_name FROM chains WHERE chain_name = ?";


    private final JdbcTemplate jdbcTemplate;


    @Override
    public Optional<Chain> findById(Long id) {
        return jdbcTemplate.query(SELECT_CHAIN, this::mapRowToChain, id).stream().findAny();
    }

    public Optional<Chain> findByName(String name) {
        return jdbcTemplate.query(SELECT_CHAIN_BY_NAME, this::mapRowToChain, name).stream().findAny();
    }

    @Override
    public Collection<Chain> findAll() {
        return jdbcTemplate.query(SELECT_ALL_CHAINS, this::mapRowToChain);
    }

    @Override
    public void save(Chain obj) {
        jdbcTemplate.update(INSERT_CHAIN, obj.getChainName());
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update(DELETE_CHAIN, id);
    }

    @Override
    public int update(Chain obj) {
        if (findById(obj.getId()).isEmpty()) {
            throw new RuntimeException(String.format("Chain with id - %d - doesn`t exist", obj.getId()));
        }
        jdbcTemplate.update(UPDATE_CHAIN, obj.getChainName());
        return 1;
    }

    private Chain mapRowToChain(ResultSet resultSet, int i) throws SQLException {
        long chain_id = resultSet.getLong("chain_id");
        return new Chain(
                chain_id,
                resultSet.getString("chain_name")
        );
    }
}
