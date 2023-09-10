package com.stockanalytics.dto;

import lombok.*;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class StatisticsDto {

    Map<String, String> valuationMeasures;

    Map<String, String> profitability;

    Map<String, String> stockPriceHistory;

    Map<String, String> shareStatistics;

    Map<String, String> incomeStatement;

    Map<String, String> balanceSheet;

    Map<String, String> cashFlowStatement;

    Map<String, String> dividendsAndSplits;

    Map<String, String> fiscalYear;
}

