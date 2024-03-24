package com.stockanalytics.model;

import lombok.*;
import jakarta.persistence.*;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Statistics {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToOne(optional = false)
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
    String nextFiscalYearEnd;
    String mostRecentQuarter;
    String lastFiscalYearEnd;
    String trailingAnnualDividendRate;
    String forwardAnnualDividendRate;
    String forwardAnnualDividendYield;
    String lastDividendDate;
    String lastDividendValue;
    String payoutRatio;
    String dividendsPerShare;
    String dividendDate;
    String exDividendDate;
    String lastSplitDate;
    String freeCashflow;
    String operatingCashflow;
    String totalAssets;
    String totalCash;
    String totalCashPerShare;
    String totalDebt;
    String debtToEquity;
    String currentRatio;
    String returnOnAssets;
    String returnOnEquity;
}