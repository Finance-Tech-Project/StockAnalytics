package com.stockanalytics.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.stockanalytics.dao.StockQuoteRepository;
import com.stockanalytics.dto.StatisticsDto;
import com.stockanalytics.model.Statistics;
import com.stockanalytics.model.StockQuote;
import com.stockanalytics.model.Symbol;
import com.stockanalytics.util.DataGetter;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Repository
public class StatisticService {
    List<String> ValuationMeasures = List.of("Market Cap", "bookValue", "enterpriseValue", "forwardPE", "pegRatio", "priceToSalesTrailing12Months", "priceToBook",
            "enterpriseToRevenue", "enterpriseToEbitda", "forwardEps", "trailingEps");
    List<String> StockPriceHistory = List.of("beta", "beta3Year", "SandP52WeekChange");
    List<String> Profitability = List.of("profitMargins", "Operating Margin (ttm)");
    List<String> ShareStatistics = List.of("Avg Vol (3 month)", "Avg Vol (10 day)", "sharesOutstanding", "impliedSharesOutstanding",
            "floatShares", "heldPercentInsiders", "heldPercentInstitutions", "sharesShort", "shortRatio", "shortPercentOfFloat", "sharesPercentSharesOut", "sharesShortPriorMonth");
    List<String> IncomeStatement = List.of("Revenue", "Revenue Per Share", "revenueQuarterlyGrowth", "Gross Profit", "EBITDA",
            "netIncomeToCommon", "Diluted EPS", "earningsQuarterlyGrowth");
    List<String> BalanceSheet = List.of("Total Cash", "Total Cash Per Share ", "Total Debt", "Total Debt/Equity", "Current Ratio",
            "Book Value Per Share");
    List<String> CashFlowStatement = List.of("Operating Cash Flow", "Levered Free Cash Flow");
    List<String> DividendsAndSplits = List.of("Forward Annual Dividend Rate", "Forward Annual Dividend Yield", "Trailing Annual Dividend Rate",
            "Trailing Annual Dividend Yield", " fiveYearAverageReturn", "threeYearAverageReturn", "lastDividendDate", "lastDividendValue", "Payout Ratio", "Dividend Date", "Ex-Dividend Date", "Last Split Factor", "lastSplitDate;   //=1654473600");
    List<String> FiscalYear = List.of("nextFiscalYearEnd", "mostRecentQuarter", "lastFiscalYearEnd");

    Map<String, String> mapValuationMeasures = new HashMap<>();
    Map<String, String> mapStockPriceHistory = new HashMap<>();
    Map<String, String> mapProfitability = new HashMap<>();
    Map<String, String> mapShareStatistics = new HashMap<>();
    Map<String, String> mapIncomeStatement = new HashMap<>();
    Map<String, String> mapBalanceSheet = new HashMap<>();
    Map<String, String> mapCashFlowStatement = new HashMap<>();
    Map<String, String> mapDividendsAndSplits = new HashMap<>();
    Map<String, String> mapFiscalYear = new HashMap<>();
    Map<String, String> mapNotFuckingNeedThis = new HashMap<>();
    Map<String, String> zeroSize = new HashMap<>();

    DecimalFormat df = new DecimalFormat("#.##");
    private final StockQuoteRepository stockQuoteRepository;
    private final DataGetter getter ;
    private final ModelMapper mapper;
    List<StockQuote> calcSortedList (Symbol symbol){
        return stockQuoteRepository
            .findAllByIdIdAndDateBetween(symbol, LocalDate.now().minusDays(365), LocalDate.now())
            .stream()
            .sorted(Comparator.comparing(StockQuote::getDate))
            .collect(Collectors.toList());
    }

    private void calcStatistics (Symbol symbol){
        List <StockQuote> sortedList = calcSortedList(symbol);
        df.setRoundingMode(RoundingMode.HALF_UP);

        Double lastPrice = sortedList.get(sortedList.size()-1).getClose();
        Double firstPrice = sortedList.get(0).getClose();
        double difference = lastPrice - firstPrice;
        String fmt = df.format(100*difference / firstPrice).concat("%");
        mapStockPriceHistory.put("52-WeekChange", fmt);
        Double minLow = calcSortedList(symbol).stream()
                .map(StockQuote::getLow)
                .min(Double::compare)
                .orElse(0.);
        Double maxHigh = calcSortedList(symbol).stream()
                .map(StockQuote::getHigh)
                .max(Double::compare)
                .orElse(0.);
        mapStockPriceHistory.put("52 Week High", df.format(maxHigh));
        mapStockPriceHistory.put("52 Week Low", df.format(minLow));
        mapStockPriceHistory.put("200-Day Moving Average", calcMovingAverage(symbol,200));
        mapStockPriceHistory.put("50-Day Moving Average", calcMovingAverage(symbol,50));
        }

    public void getStatisticsFromYahoo(Symbol symbol) throws JsonProcessingException {
        Map<String, String> receivedData = getter.getDataForStatistics(symbol);

        for (Map.Entry<String, String> entry : receivedData.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (value == null || value.equals("0")){
                zeroSize.put(key, value);
            }else if(ValuationMeasures.contains(key)) {
                mapValuationMeasures.put(key, value);
            } else if (StockPriceHistory.contains(key)) {
                mapStockPriceHistory.put(key, value);
            } else if (Profitability.contains(key)) {
                mapProfitability.put(key, value);
            } else if (ShareStatistics.contains(key)) {
                mapShareStatistics.put(key, value);
            } else if (IncomeStatement.contains(key)) {
                mapIncomeStatement.put(key, value);
            } else if (BalanceSheet.contains(key)) {
                mapBalanceSheet.put(key, value);
            } else if (CashFlowStatement.contains(key)) {
                mapCashFlowStatement.put(key, value);
            } else if (DividendsAndSplits.contains(key)) {
                mapDividendsAndSplits.put(key, value);
            } else if (FiscalYear.contains(key)) {
                mapFiscalYear.put(key, value);
            } else {
                mapNotFuckingNeedThis.put(key, value);
            }
        }
    }

    private String calcMovingAverage(Symbol symbol, int days) {
        df.setRoundingMode(RoundingMode.HALF_UP);
        double movingAverage = calcSortedList(symbol).stream()
                .filter(quot -> quot.getDate().isAfter(LocalDate.now().minusDays(days)))
                .mapToDouble(StockQuote::getClose)
                .average().orElse(0.);

        return df.format(movingAverage);
    }
    public Statistics getStatistics (Symbol symbol) throws JsonProcessingException {
        getStatisticsFromYahoo(symbol);
        calcStatistics(symbol);
        return Statistics.builder()
                .symbol(symbol)
                .valuationMeasures(mapValuationMeasures)
                .shareStatistics(mapShareStatistics)
                .stockPriceHistory(mapStockPriceHistory)
                .fiscalYear(mapFiscalYear)
                .incomeStatement(mapIncomeStatement)
                .balanceSheet(mapBalanceSheet)
                .cashFlowStatement(mapCashFlowStatement)
                .dividendsAndSplits(mapDividendsAndSplits)
                .profitability(mapProfitability)
                .build();
    }

    public StatisticsDto getStatisticsDto(Symbol symbol) throws JsonProcessingException {
        return mapper.map(getStatistics(symbol), StatisticsDto.class);
    }
}
