package ru.varfolomeev.retail_test.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class Customer {
    private Long id;
    private Integer code;
    private String name;
    private Chain chain;
}
