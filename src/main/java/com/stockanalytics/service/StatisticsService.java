package com.stockanalytics.service;

import com.stockanalytics.dao.StatisticsRepository;
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
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("CallToPrintStackTrace")
@RequiredArgsConstructor
@Service
@Repository
public class StatisticsService {

    final DecimalFormat df = new DecimalFormat("#.##");
    private final StockQuoteService stockQuoteService;
    private final SymbolService symbolService;
    private final StatisticsRepository statisticsRepository;
    private final DataGetter getter;
    private final ModelMapper mapper;

    public void updateStatistics() throws IOException, InterruptedException {
        List<Statistics> listStat = statisticsRepository.findAll();
        for (Statistics stat : listStat) {
            statisticsRepository.delete(stat);
            Statistics newStat = getNewStatistics(stat.getSymbol());
            statisticsRepository.save(newStat);
        }
    }

    private String calcMovingAverage(List<StockQuoteDto> list, int days) {
        double movingAverage = list.stream()
                .filter(quot -> quot.getDate().isAfter(LocalDate.now().minusDays(days)))
                .mapToDouble(StockQuoteDto::getClose)
                .average().orElse(0.);
        return df.format(movingAverage);
    }

    private Map<String, String> calcStatistics(Symbol symbol) {
        Map<String, String> calcMap = new HashMap<>();
        List<StockQuoteDto> sortedList = stockQuoteService.getData(symbol, LocalDate.now().minusDays(365), LocalDate.now());
        if (!sortedList.isEmpty()) {
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
            calcMap.put("twoHundredDaysMovingAverage", calcMovingAverage(sortedList, 200));
            calcMap.put("fiftyDaysMovingAverage", calcMovingAverage(sortedList, 50));
            return calcMap;
        }
        return null;
    }

    public Map<String, String> getStatisticsFromYahoo(Symbol symbol) {
        return getter.getDataForStatisticsFromYahoo(symbol);
    }

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    public Statistics getNewStatistics(Symbol symbol) throws IOException, InterruptedException {
        Statistics stat = new Statistics();
        List<Map<String, String>> mapList = getter.getDataForAnalysisFromRapidAPI(symbol);
        List<Map<String, String>> statData = new ArrayList<>(mapList);
        statData.add(getStatisticsFromRapidAPI(symbol));
        statData.add(getStatisticsFromYahoo(symbol));
        statData.add(calcStatistics(symbol));
        HashMap<String, String> parameters = new HashMap<>();
        for (Map<String, String> map : statData) {
            if (map != null) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    parameters.put(key, value);
                }
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
        for (Map.Entry<String, Object> entry : receivedData.entrySet()) {
            if (entry.getValue() instanceof String) {
                parameters.put(entry.getKey(), (String) entry.getValue());
            } else if (entry.getValue() instanceof Double) {
                if ((Double) entry.getValue() > 1000) {
                    Number numberValue = (Number) entry.getValue();
                    Long l = numberValue.longValue();
                    entry.setValue(l);
                } else {
                    parameters.put(entry.getKey(), df.format(entry.getValue()));
                }
            }
            if (entry.getValue() instanceof Integer || entry.getValue() instanceof Long) {
                Number numberValue = (Number) entry.getValue();
                Long l = numberValue.longValue();
                if (entry.getKey().contains("Date") || entry.getKey().contains("Time")) {
                    Instant instant = Instant.ofEpochSecond(l);
                    LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    parameters.put(entry.getKey(), localDate.format(formatter));
                } else {
                    double d;
                    if (l > 999_999_999_999L) {
                        l = l / 1_000_000;
                        d = l / 1000000.;
                        String str = df.format(d).concat("T");
                        parameters.put(entry.getKey(), str);
                    } else if (Math.abs(l) > 999_999_999) {
                        d = l / 1_000_000_000.;
                        String str = df.format(d).concat("B");
                        parameters.put(entry.getKey(), str);
                    } else if (Math.abs(l) > 999_999) {
                        d = l / 1_000_000.;
                        String str = df.format(d).concat("M");
                        parameters.put(entry.getKey(), str);
                    } else {
                        parameters.put(entry.getKey(), String.valueOf(l));
                    }
                }
            }
        }
        return parameters;
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public StatisticsDto getStatisticsDto(String ticker) throws IOException, InterruptedException {
        Symbol symbol = symbolService.getSymbol(ticker);
        Statistics st;
        StatisticsDto dto = new StatisticsDto();
        if (statisticsRepository.existsBySymbol(symbol)) {
            st = statisticsRepository.findBySymbol(symbol).get();
        } else {
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

