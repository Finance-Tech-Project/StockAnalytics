package com.stockanalytics.dto.statistcs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BalanceSheet{
    String totalAssets;
    String totalCash;
    String totalCashPerShare;
    String totalDebt;
    String debtToEquity;
    String currentRatio;
    String bookValuePerShare;
}
