package ru.varfolomeev.retail_test.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class Price {
    private Long id;
    private Product product;
    private BigDecimal regularPrice;
    private Chain chain;
}
