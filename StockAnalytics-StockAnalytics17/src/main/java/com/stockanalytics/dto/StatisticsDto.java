package com.stockanalytics.dto;

import com.stockanalytics.dto.statistcs.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class StatisticsDto {
    private ValuationMeasures valuationMeasures;
    Profitability profitability;
    StockPriceHistory stockPriceHistory;
    ShareStatistics shareStatistics;
    IncomeStatement incomeStatement;
    BalanceSheet balanceSheet;
    CashFlowStatement cashFlowStatement;
    DividendsAndSplits dividendsAndSplits;
    FiscalYear fiscalYear;
}

