package com.stockanalytics.dto.statistcs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IncomeStatement{
    String revenue;
    String revenuePerShare;
    String revenueQuarterlyGrowth;
    String GrossProfit;
    String EBITDA;
    String netIncomeToCommon;
    String dilutedEPS;
    String earningsQuarterlyGrowth;
}
