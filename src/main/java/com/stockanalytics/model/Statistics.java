package com.stockanalytics.model;

import lombok.*;
import javax.persistence.*;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Statistics {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "symbol_id")
    Symbol symbol;

    String marketCap;
    String bookValue;
    String enterpriseValue;
    String forwardPE;
    String pegRatio;
    String priceToSalesTrailing12Months;
    String priceToBook;
    String enterpriseToRevenue;
    String enterpriseToEbitda;
    String forwardEps;
    String trailingEps;
    String beta;
    String beta3Year;
    String SandP52WeekChange;
    String FiftyTowWeekChange;
    String FiftyTowWeekHigh;
    String FiftyTowWeekLow;
    String FiftyTowWeekSPChange;
    String FiftyDaysMovingAverage;
    String twoHundredDaysMovingAverage;
    String sharesOutstanding;
    String impliedSharesOutstanding;
    String floatShares;
    String heldPercentInsiders;
    String heldPercentInstitutions;
    String sharesShort;
    String shortRatio;
    String shortPercentOfFloat;
    String sharesPercentSharesOut;
    String sharesShortPriorMonth;
    String profitMargins;
    String operatingMargin;
    String revenue;
    String revenuePerShare;
    String revenueQuarterlyGrowth;
    String GrossProfit;
    String EBITDA;
    String netIncomeToCommon;
    String dilutedEPS;
    String earningsQuarterlyGrowth;
    String nextFiscalYearEnd;
    String mostRecentQuarter;
    String lastFiscalYearEnd;
    String forwardAnnualDividendRate;
    String forwardAnnualDividendYield;
    String trailingAnnualDividendRate;
    String trailingAnnualDividendYield;
    String fiveYearAverageReturn;
    String threeYearAverageReturn;
    String lastDividendDate;
    String lastDividendValue;
    String payOutRatio;
    String dividendDate;
    String exDividendDate;
    String lastSplitFactor;
    String lastSplitDate;
    String operatingCashFlow;
    String leveredFreeCashFlow;
    String totalCash;
    String totalCashPerShare;
    String totalDebt;
    String totalDebtToEquity;
    String currentRatio;
    String bookValuePerShare;

}

