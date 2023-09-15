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
//    String exDividendDate; //exDividendDate
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

//dividendYield=0,54, regularMarketDayRange=173.98 - 177.3, earningsTimestampEnd=1698667200, targetPriceLow=138,6,fiftyDayAverageChange=-11,16,
//exchangeDataDelayedBy=0,averageDailyVolume3Month=57331690,firstTradeDateMilliseconds=345,48B,dividendRate=0,96,regularMarketVolume=83919060,priceHint=2,
// priceEpsNextQuarter=88,43,epsTrailingTwelveMonths=5,96,epsNextQuarter=1,97,epsForward=6,14,targetPriceMean=186,05, trailingPE=29,23priceEpsCurrentYear=30,89,
//epsCurrentYear=5,64, dividendsPerShare=0,93