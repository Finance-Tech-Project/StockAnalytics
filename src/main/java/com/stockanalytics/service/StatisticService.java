package com.stockanalytics.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.stockanalytics.dao.StockQuoteRepository;
import com.stockanalytics.dto.StatisticsDTO;
import com.stockanalytics.model.StockQuote;
import com.stockanalytics.model.Symbol;
import com.stockanalytics.util.DataGetter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Repository
public class StatisticService {
    List<Map<String, Object>> list;
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
    List<String> notFuckingNeedThis = List.of("priceHint", "dateShortInterest", "morningStarOverallRating", "otalAssets", "lastCapGain",
            "yield", "annualReportExpenseRatio", "fundInceptionDate", "ytdReturn", "annualHoldingsTurnover", "morningStarRiskRating");
    Map<String, Object> mapValuationMeasures = new HashMap<>();
    Map<String, Object> mapStockPriceHistory = new HashMap<>();
    Map<String, Object> mapProfitability = new HashMap<>();
    Map<String, Object> mapShareStatistics = new HashMap<>();
    Map<String, Object> mapIncomeStatement = new HashMap<>();
    Map<String, Object> mapBalanceSheet = new HashMap<>();
    Map<String, Object> mapCashFlowStatement = new HashMap<>();
    Map<String, Object> mapDividendsAndSplits = new HashMap<>();
    Map<String, Object> mapFiscalYear = new HashMap<>();
    Map<String, Object> mapNotFuckingNeedThis = new HashMap<>();
    Map<String, Object> zeroSize = new HashMap<>();

    private final StockQuoteRepository stockQuoteRepository;
    private final DataGetter getter ;


    List<StockQuote> calcSortedList (Symbol symbol){
        return stockQuoteRepository
            .findAllByIdIdAndDateBetween(symbol, LocalDate.now().minusDays(365), LocalDate.now())
            .stream()
            .sorted(Comparator.comparing(StockQuote::getDate))
            .collect(Collectors.toList());
    }

    private HashMap<String, Object> mappingDouble(Optional<Double> x) {
        HashMap<String, Object> map = new HashMap<>();
        if (x.isPresent()) {
            map.put("raw", x.get());
            map.put("fmt", Double.toString(x.get()));
        }
        return map;
    }

    public void calcStatistics (Symbol symbol){
        List <StockQuote> sortedList = calcSortedList(symbol);
         Double lastPrice = sortedList.get(sortedList.size()-1).getClose();
        Double firstPrice = sortedList.get(0).getClose();

        Double difference = lastPrice - firstPrice;
            double proc = difference / firstPrice;
            HashMap<String, Object> map = new HashMap<>();
            map.put("raw", proc);
            map.put("fmt", Double.toString(proc * 100).concat("%"));
            mapStockPriceHistory.put("52-WeekChange", map);
        Optional<Double> minLow = calcSortedList(symbol).stream()
                .map(StockQuote::getLow)
                .min(Double::compare);
        Optional<Double> maxHigh = calcSortedList(symbol).stream()
                    .map(StockQuote::getHigh)
                    .max(Double::compare);
            mapStockPriceHistory.put("52 Week High", mappingDouble(maxHigh));

            mapStockPriceHistory.put("52 Week Low", mappingDouble(minLow));
            mapStockPriceHistory.put("200-Day Moving Average", calcMovingAverage(symbol,200));
            mapStockPriceHistory.put("50-Day Moving Average", calcMovingAverage(symbol,50));
        }

    public void getStatisticsFromYahoo(Symbol symbol) throws JsonProcessingException {
        Map<String, Object> receivedData = getter.getDataForStatistics(symbol);

        for (Map.Entry<String, Object> entry : receivedData.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof LinkedHashMap && ((LinkedHashMap<?, ?>) value).isEmpty()){
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

    public Map<String, Object> calcMovingAverage(Symbol symbol, int days){
        HashMap<String, Object> hashMap = new HashMap<>();
        OptionalDouble movingAverage = calcSortedList(symbol).stream()
                .filter(quot -> quot.getDate().isAfter(LocalDate.now().minusDays(days)))
                .mapToDouble(StockQuote::getClose)
                .average();
        if (movingAverage.isPresent()) {
            hashMap.put("raw", movingAverage.getAsDouble());
            hashMap.put("fmt", Double.toString(movingAverage.getAsDouble()));
        }
        return hashMap;
    }
    public StatisticsDTO getStatistics (Symbol symbol) throws JsonProcessingException {
        Map<String, Map<String, Object>> map = new HashMap<>();

        getStatisticsFromYahoo(symbol);
        calcStatistics(symbol);
        map.put("ValuationMeasures", mapValuationMeasures);
        map.put("ShareStatistics", mapShareStatistics);
        map.put("StockPriceHistory", mapStockPriceHistory);
        map.put("FiscalYear", mapFiscalYear);
        map.put("IncomeStatement", mapIncomeStatement);
         return new  StatisticsDTO(map);
    }
}
