package com.stockanalytics.dto.statistcs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IncomeStatement{
    String revenue;
    String revenuePerShare;
    String revenueGrowth;
    String grossProfits;
    String epsTrailingTwelveMonths;
    String ebitda;
    String ebitdaMargins;
    String netIncomeToCommon;
//    String dilutedEPS;
//    String earningsQuarterlyGrowth;
}
