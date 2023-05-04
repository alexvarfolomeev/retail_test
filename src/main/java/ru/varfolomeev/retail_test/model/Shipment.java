package ru.varfolomeev.retail_test.model;

import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Shipment {
    private Long id;
    private Date shipmentDate;
    private Product product;
    private Customer customer;
    private Chain chain;
    private Integer volume;
    private BigDecimal salesValue;
    private PromoSign promoSign;
}
