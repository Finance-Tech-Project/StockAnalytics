package com.stockanalytics.model;

import lombok.*;
import javax.persistence.*;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "statistics")
public class Statistics {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "symbol_name")
    Symbol symbol;

    String marketCap;
    String bookValue;
    String enterpriseValue;
    String forwardPE;
    String trailingPE;
    String pegRatio;
    String quickRatio;
    String priceToSales;
    String priceToBook;
    String enterpriseToRevenue;
    String enterpriseToEbitda;
    String forwardEps;
    String trailingEps;
    String epsCurrentYear;
    String epsTrailingTwelveMonths;
    String beta;
    String SandP52WeekChange;
    String fiftyTowWeekChange;
    String fiftyTowWeekHigh;
    String fiftyTowWeekLow;
    String fiftyDaysMovingAverage;
    String twoHundredDaysMovingAverage;
    String trailingAnnualDividendYield;
    String sharesOutstanding;
    String bookValuePerShare;
    String impliedSharesOutstanding;
    String floatShares;
    String heldPercentInsiders;
    String heldPercentInstitutions;
    String sharesShort;
    String shortRatio;
    String shortPercentFloat;
    String shortPercentOfFloat;
    String sharesPercentSharesOut;
    String sharesShortPriorMonth;
    String sharesShortPrevMonth;
    String sharesShortPreviousMonthDate;
    String averageVolume10days;
    String averageDailyVolume10Day;
    String averageDailyVolume3Month;
//    String ytdReturn;
//    String yield;
    String profitMargins;
    String operatingMargins;
    String grossMargins;
    String revenue;
    String revenuePerShare;
    String revenueGrowth;
    String grossProfits;
    String ebitda;
    String ebitdaMargins;
    String netIncomeToCommon;
//    String dilutedEPS;
//    String earningsQuarterlyGrowth;
    String nextFiscalYearEnd;
    String mostRecentQuarter;
    String lastFiscalYearEnd;
    String trailingAnnualDividendRate;
    String forwardAnnualDividendRate;
    String forwardAnnualDividendYield;
//    String fiveYearAverageReturn;
//    String threeYearAverageReturn;
    String lastDividendDate;
    String lastDividendValue;
    String payoutRatio;
    String dividendsPerShare;
    String dividendDate;
    String exDividendDate;
//    String lastSplitFactor;
    String lastSplitDate;
    String freeCashflow;
    String operatingCashflow;
//    String leveredFreeCashFlow;
    String totalAssets;
    String totalCash;
    String totalCashPerShare;
    String totalDebt;
    String debtToEquity;
    String currentRatio;
    String returnOnAssets;
    String returnOnEquity;

}