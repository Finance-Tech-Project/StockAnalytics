package com.stockanalytics.dto.statistcs;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValuationMeasures {
    String marketCap;
    String bookValue;
    String enterpriseValue;
    String forwardPE;
    String trailingPE;
    String pegRatio;
    String priceToSales;
    String priceToBook;
    String enterpriseToRevenue;
    String enterpriseToEbitda;
    String epsCurrentYear;
    String forwardEps;
    String trailingEps;
    String quickRatio;
}