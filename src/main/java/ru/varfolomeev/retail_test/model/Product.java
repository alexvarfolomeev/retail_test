package ru.varfolomeev.retail_test.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    private Long id;
    private Long code;
    private String description;
    private String categoryCode;
    private String categoryName;
}
