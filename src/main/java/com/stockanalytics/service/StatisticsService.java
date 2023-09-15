package com.stockanalytics.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.stockanalytics.dao.StatisticsRepository;
import com.stockanalytics.dao.StockQuoteRepository;
import com.stockanalytics.dto.StatisticsDto;
import com.stockanalytics.dto.StockQuoteDto;
import com.stockanalytics.dto.statistcs.Profitability;
import com.stockanalytics.dto.statistcs.ShareStatistics;
import com.stockanalytics.dto.statistcs.StockPriceHistory;
import com.stockanalytics.dto.statistcs.ValuationMeasures;
import com.stockanalytics.model.Statistics;
import com.stockanalytics.model.Symbol;
import com.stockanalytics.util.DataGetter;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Repository
public class StatisticsService {
    List<String> listValuationMeasures = List.of("Market Cap", "bookValue", "enterpriseValue", "forwardPE", "pegRatio", "priceToSalesTrailing12Months", "priceToBook",
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
            "Trailing Annual Dividend Yield", " fiveYearAverageReturn", "threeYearAverageReturn", "lastDividendDate", "lastDividendValue", "Payout Ratio", "Dividend Date", "Ex-Dividend Date", "Last Split Factor", "lastSplitDate"  );
    List<String> FiscalYear = List.of("nextFiscalYearEnd", "mostRecentQuarter", "lastFiscalYearEnd");



    DecimalFormat df = new DecimalFormat("#.##");
    private final StockQuoteRepository stockQuoteRepository;
    private final StockQuoteService stockQuoteService;
    private final SymbolService symbolService;
    private  final StatisticsRepository statisticsRepository;
    private final DataGetter getter;
    private final ModelMapper mapper;
//    Statistics stat = new Statistics();


    private double round (double number){
        String str = df.format(number).replace(",", ".");
        return Double.parseDouble(str);
    }

    public void updateStatistics() throws IOException, InterruptedException {
        List<Statistics> listStat = statisticsRepository.findAll();
        for (Statistics stat : listStat) {
            statisticsRepository.delete(stat);
            Statistics newStat = getNewStatistics(stat.getSymbol());
//           stat = statisticsRepository.findBySymbol(stat.getSymbol()).get();
//            if (newStat != null) {
//                statisticsRepository.delete(stat);
                statisticsRepository.save(newStat);
//            }
        }
    }

    private String calcMovingAverage(List <StockQuoteDto> list, Symbol symbol, int days) {
        double movingAverage = list.stream()
                .filter(quot -> quot.getDate().isAfter(LocalDate.now().minusDays(days)))
                .mapToDouble(StockQuoteDto::getClose)
                .average().orElse(0.);
        return df.format(movingAverage);
    }

    private Map<String, String> calcStatistics(Symbol symbol) {
        Map<String, String> calcMap = new HashMap<>();
        List<StockQuoteDto> sortedList = stockQuoteService.getData(symbol, LocalDate.now().minusDays(365), LocalDate.now())
                .stream()
                .sorted(Comparator.comparing(StockQuoteDto::getDate))
                .collect(Collectors.toList());
        df.setRoundingMode(RoundingMode.HALF_UP);
        Double lastPrice = sortedList.get(sortedList.size() - 1).getClose();
        Double firstPrice = sortedList.get(0).getClose();
        double difference = lastPrice - firstPrice;
        String fmt = df.format(100 * difference / firstPrice).concat("%");
        calcMap.put("fiftyTowWeekChange", fmt);
        Double minLow = sortedList.stream()
                .map(StockQuoteDto::getLow)
                .min(Double::compare)
                .orElse(0.);
        Double maxHigh = sortedList.stream()
                .map(StockQuoteDto::getHigh)
                .max(Double::compare)
                .orElse(0.);
        calcMap.put("fiftyTowWeekHigh", df.format(maxHigh));
        calcMap.put("fiftyTowWeekLow", df.format(minLow));
        calcMap.put("twoHundredDaysMovingAverage", calcMovingAverage(sortedList, symbol, 200));
        calcMap.put("fiftyDaysMovingAverage", calcMovingAverage(sortedList, symbol, 50));
        return calcMap;
    }


    public Map<String, String> getStatisticsFromYahoo(Symbol symbol) throws JsonProcessingException {
        return getter.getDataForStatisticsFromYahoo(symbol);
    }

        public Statistics getNewStatistics(Symbol symbol) throws IOException, InterruptedException {
            Statistics stat = new Statistics();
            List<Map<String, String>> statData = new ArrayList<>();
            List <Map<String, String>> mapList = getter.getDataForAnalisisFromRapidAPI(symbol);
            statData.addAll(mapList);
            statData.add(getStatisticsFromRapidAPI(symbol));
            statData.add(getStatisticsFromYahoo(symbol))      ;
            statData.add(calcStatistics(symbol));
            HashMap<String, String> parameters = new HashMap<>();
            for (Map<String, String> map : statData) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    parameters.put(key, value);
                }
            }
            List<String> missingKeys = new ArrayList<>();
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (value != null && !key.equals("symbol")) {
                    try {
                        Field field = stat.getClass().getDeclaredField(key);
                        field.setAccessible(true);
                        field.set(stat, value);
                    } catch (NoSuchFieldException e) {
                        missingKeys.add(key);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            stat.setSymbol(symbol);
            return stat;
        }

    private Map<String, String> getStatisticsFromRapidAPI(Symbol symbol) throws IOException, InterruptedException {
        df.setRoundingMode(RoundingMode.HALF_UP);
        Map<String, Object> receivedData = getter.getDataForStatisticsFromRapidAPI(symbol);
        Map<String, String> parameters = new HashMap<>();
        for (Map.Entry<String,  Object> entry : receivedData.entrySet()) {
            if(entry.getValue() instanceof String){
                parameters.put(entry.getKey(), (String) entry.getValue());
            }else if (entry.getValue() instanceof Integer) {
           String str = String.valueOf(entry.getValue());
           parameters.put(entry.getKey(), str);
            }else if(entry.getValue() instanceof Double){
                parameters.put(entry.getKey(), df.format(entry.getValue()));
            }else if(entry.getValue() instanceof Long){
                long l;
                double d;
                if( Math.abs((Long) entry.getValue()) > 999_999_999_999L){
                    l =  ((long) entry.getValue()) / 1_000_000;
                    d = l/1000000.;
                    String str = df.format(d).concat("T");
                    parameters.put(entry.getKey(), str);
                }else  if  (Math.abs((long)entry.getValue()) > 999_999_999){
                    d =  ((long)entry.getValue()) / 1_000_000_000.;
                    String str = df.format(d).concat("B");
                    parameters.put(entry.getKey(), str);
                }else{
                    d = ((long) entry.getValue()) / 1_000_000.;
                    String str = df.format(d).concat("M");
                    parameters.put(entry.getKey(), str);
                }
            }
        }
        return parameters;
    }

    public StatisticsDto getStatisticsDto (String ticker) throws IOException, InterruptedException {
            Symbol symbol = symbolService.getSymbol(ticker);
            Statistics st = new Statistics();
            StatisticsDto dto =  new StatisticsDto();
            if(statisticsRepository.existsBySymbol(symbol)) {
                Optional o = statisticsRepository.findBySymbol(symbol);
                if(o.isPresent()) {
                    st = (Statistics) o.get();
                }
            }else{
                st = getNewStatistics(symbol);
                statisticsRepository.save(st);
            }
            dto.setValuationMeasures(mapper.map(st, ValuationMeasures.class));
            dto.setStockPriceHistory(mapper.map(st, StockPriceHistory.class));
            dto.setBalanceSheet(mapper.map(st, com.stockanalytics.dto.statistcs.BalanceSheet.class));
            dto.setShareStatistics(mapper.map(st, ShareStatistics.class));
            dto.setCashFlowStatement(mapper.map(st, com.stockanalytics.dto.statistcs.CashFlowStatement.class));
            dto.setDividendsAndSplits(mapper.map(st, com.stockanalytics.dto.statistcs.DividendsAndSplits.class));
            dto.setIncomeStatement(mapper.map(st, com.stockanalytics.dto.statistcs.IncomeStatement.class));
            dto.setFiscalYear(mapper.map(st, com.stockanalytics.dto.statistcs.FiscalYear.class));
            dto.setProfitability(mapper.map(st, Profitability.class));
            return dto;
        }
    }

