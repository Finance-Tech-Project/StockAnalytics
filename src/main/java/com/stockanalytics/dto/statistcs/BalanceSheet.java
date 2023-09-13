package com.stockanalytics.dto.statistcs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BalanceSheet{
    String totalCash;
    String totalCashPerShare;
    String totalDebt;
    String totalDebtToEquity;
    String currentRatio;
    String bookValuePerShare;
}
