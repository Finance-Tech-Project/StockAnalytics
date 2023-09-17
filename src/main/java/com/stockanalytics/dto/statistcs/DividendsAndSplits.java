package com.stockanalytics.dto.statistcs;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DividendsAndSplits {
    String forwardAnnualDividendRate;
    String forwardAnnualDividendYield;
    String trailingAnnualDividendRate;
    String trailingAnnualDividendYield;
//    String fiveYearAverageReturn;
//    String threeYearAverageReturn;
    String lastDividendDate;
    String lastDividendValue;
    String payoutRatio;
    String dividendDate;
//    String exDividendDate;
//    String lastSplitFactor;
    String lastSplitDate;
    String dividendsPerShare;

}
