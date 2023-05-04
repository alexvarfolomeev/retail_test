package ru.varfolomeev.retail_test.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.varfolomeev.retail_test.model.AnalysisResponse;
import ru.varfolomeev.retail_test.model.PromoSign;
import ru.varfolomeev.retail_test.model.Shipment;
import ru.varfolomeev.retail_test.repository.ShipmentAnalysis;
import ru.varfolomeev.retail_test.repository.ShipmentRepository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ShipmentRepositoryImpl implements ShipmentRepository, ShipmentAnalysis {

    public static final String SELECT_SHIPMENT =
            "SELECT shipment_id, shipment_date, product_id, customer_id, chain_id, volume, sales_value, promo_sign FROM shipments"
                    + " WHERE shipment_id = ?";
    private static final String SELECT_ALL_SHIPMENTS =
            "SELECT shipment_id, shipment_date, product_id, customer_id, volume, sales_value, promo_sign FROM shipments";

    private static final String INSERT_SHIPMENT =
            "INSERT INTO shipments (shipment_date, product_id, customer_id, volume, sales_value, promo_sign) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_SHIPMENT =
            "UPDATE shipments SET shipment_date = ?, product_id = ?, customer_id = ?, volume = ?, sales_value = ?, promo_sign = ?"
                    + " WHERE shipment_id = ?";
    private static final String DELETE_SHIPMENT = "DELETE FROM shipments WHERE shipment_id = ?";

    public static final String SELECT_SHIPMENTS_WITH_PROMO_SIGN =
            "SELECT c.chain_name AS chain, pr.product_category_code AS category, month(sh.shipment_date) AS month, year(sh.shipment_date) AS year, sh.sales_value AS value, \n" +
                    "SUM(case when sh.promo_sign = 'REGULAR' then volume else 0 end) as regular,\n" +
                    "SUM(case when sh.promo_sign = 'PROMO' then volume else 0 end) as promo,\n" +
                    "((SUM(case when sh.promo_sign = 'PROMO' then volume else 0 end) * 100) / (SUM(case when sh.promo_sign = 'PROMO' then volume else 0 end) + SUM(case when sh.promo_sign = 'REGULAR' then volume else 0 end))) AS percent \n" +
                    "FROM shipments AS sh \n" +
                    "INNER JOIN chains AS c ON  sh.chain_id = c.chain_id\n" +
                    "INNER JOIN products AS pr ON  sh.product_id = pr.product_id\n" +
                    "group by month;";

    public static final String SELECT_SHIPMENTS_WITH_CHAIN_AND_PRODUCT_FILTER =
            "SELECT sh.shipment_date AS date, c.chain_name AS chain, pr.product_category_code AS category, sh.sales_value AS sales_value, sh.volume AS volume,\n" +
                    "pr.product_code AS product, cm.customer_code AS customer, sh.promo_sign AS promo_sign\n" +
                    "FROM shipments AS sh\n" +
                    "INNER JOIN chains AS c ON  sh.chain_id = c.chain_id\n" +
                    "INNER JOIN products AS pr ON  sh.product_id = pr.product_id\n" +
                    "INNER JOIN customers AS cm ON  sh.customer_id = cm.customer_id\n" +
                    "WHERE sh.product_id IN (:products)\n" +
                    "AND sh.chain_id IN (:chains);";

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final ChainRepositoryImpl chainRepository;

    private final ProductRepositoryImpl productRepository;

    private final CustomerRepositoryImpl customerRepository;

    @Override
    public Optional<Shipment> findById(Long id) {
        return jdbcTemplate.query(SELECT_SHIPMENT, this::mapRowToShipment, id).stream().findAny();
    }

    @Override
    public Collection<Shipment> findAll() {
        return jdbcTemplate.query(SELECT_ALL_SHIPMENTS, this::mapRowToShipment);
    }

    @Override
    public void save(Shipment obj) {
        jdbcTemplate.update(INSERT_SHIPMENT, obj.getShipmentDate(), obj.getProduct().getId(), obj.getCustomer().getId(), obj.getVolume(), obj.getSalesValue(), obj.getPromoSign());
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update(DELETE_SHIPMENT, id);
    }

    @Override
    public int update(Shipment obj) {
        if (findById(obj.getId()).isEmpty()) {
            throw new RuntimeException(String.format("Product with id - %d - doesn`t exist", obj.getId()));
        }
        jdbcTemplate.update(UPDATE_SHIPMENT, obj.getShipmentDate(), obj.getProduct().getId(), obj.getChain().getId(), obj.getVolume(), obj.getSalesValue(), obj.getPromoSign());
        return 1;
    }

    private Shipment mapRowToShipment(ResultSet resultSet, int i) throws SQLException {
        long shipmentId = resultSet.getLong("shipment_id");
        return new Shipment(
                shipmentId,
                resultSet.getDate("shipment_date"),
                productRepository.findById(resultSet.getLong("product_id")).orElseThrow(),
                customerRepository.findById(resultSet.getLong("customer_id")).orElseThrow(),
                chainRepository.findById(resultSet.getLong("chain_id")).orElseThrow(),
                resultSet.getInt("volume"),
                new BigDecimal(resultSet.getString("sales_value")),
                PromoSign.valueOf(resultSet.getString("promo_sign"))
        );
    }

    @Override
    public Collection<AnalysisResponse> getAnalysisWithPromoSign() {
        return jdbcTemplate.query(SELECT_SHIPMENTS_WITH_PROMO_SIGN, (rs, rowNum) ->
                AnalysisResponse.builder()
                        .chainName(rs.getString("chain"))
                        .categoryCode(rs.getInt("category"))
                        .month(rs.getInt("month"))
                        .year(rs.getInt("year"))
                        .salesValue(rs.getInt("value"))
                        .regularVolume(rs.getInt("regular"))
                        .promoVolume(rs.getInt("promo"))
                        .percent(rs.getDouble("percent"))
                        .build()
        );
    }

    @Override
    public Collection<AnalysisResponse> getAnalysisWithChainsAndProductsFilter(List<Integer> chains, List<Integer> products) {
        var params = new HashMap<String, Object>();
        params.put("chains", chains);
        params.put("products", products);
        return namedParameterJdbcTemplate.query(
                SELECT_SHIPMENTS_WITH_CHAIN_AND_PRODUCT_FILTER,
                params,
                (rs, rowNum) ->
                        AnalysisResponse.builder()
                                .date(rs.getDate("date"))
                                .productCode(rs.getLong("product"))
                                .customerCode(rs.getInt("customer"))
                                .chainName(rs.getString("chain"))
                                .categoryCode(rs.getInt("category"))
                                .salesValue(rs.getInt("sales_value"))
                                .totalVolume(rs.getInt("volume"))
                                .promoSign(PromoSign.valueOf(rs.getString("promo_sign")))
                                .build()
        );
    }
}
