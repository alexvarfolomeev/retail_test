package ru.varfolomeev.retail_test.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class AnalysisResponse {
    private Date date;
    private String chainName;
    private Integer categoryCode;
    private Long productCode;
    private Integer customerCode;
    private Integer month;
    private Integer year;
    private Integer salesValue;
    private Integer regularVolume;
    private Integer promoVolume;
    private Integer totalVolume;
    private Double percent;
    private PromoSign promoSign;
}
