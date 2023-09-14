package com.stockanalytics.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.stockanalytics.dao.StatisticsRepository;
import com.stockanalytics.dao.StockQuoteRepository;
import com.stockanalytics.dto.StatisticsDto;
import com.stockanalytics.dto.statistcs.Profitability;
import com.stockanalytics.dto.statistcs.ShareStatistics;
import com.stockanalytics.dto.statistcs.StockPriceHistory;
import com.stockanalytics.dto.statistcs.ValuationMeasures;
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
import java.util.Comparator;
import java.util.List;
import java.util.Map;
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
    private final SymbolService symbolService;
    private  final StatisticsRepository statisticsRepository;
    private final DataGetter getter;
    private final ModelMapper mapper;
    Statistics stat = new Statistics();

    public void updateStatistics() throws JsonProcessingException {
        List<Statistics> listStat = statisticsRepository.findAll();
        for (Statistics stat : listStat) {
            Statistics newStat = getNewStatistics(stat.getSymbol());
           stat = statisticsRepository.findBySymbol(stat.getSymbol()).get();
            if (newStat != null) {
                statisticsRepository.delete(stat);
                statisticsRepository.saveAndFlush(newStat);
            }
        }
    }

    private String calcMovingAverage(List <StockQuote> list, Symbol symbol, int days) {
        double movingAverage = list.stream()
                .filter(quot -> quot.getDate().isAfter(LocalDate.now().minusDays(days)))
                .mapToDouble(StockQuote::getClose)
                .average().orElse(0.);
        return df.format(movingAverage);
    }

    private void calcStatistics(Symbol symbol) {
        List<StockQuote> sortedList = stockQuoteRepository
                .findAllByIdIdAndDateBetween(symbol, LocalDate.now().minusDays(365), LocalDate.now())
                .stream()
                .sorted(Comparator.comparing(StockQuote::getDate))
                .collect(Collectors.toList());
        df.setRoundingMode(RoundingMode.HALF_UP);
        Double lastPrice = sortedList.get(sortedList.size() - 1).getClose();
        Double firstPrice = sortedList.get(0).getClose();
        double difference = lastPrice - firstPrice;
        String fmt = df.format(100 * difference / firstPrice).concat("%");
        stat.setFiftyTowWeekChange(fmt);
        Double minLow = sortedList.stream()
                .map(StockQuote::getLow)
                .min(Double::compare)
                .orElse(0.);
        Double maxHigh = sortedList.stream()
                .map(StockQuote::getHigh)
                .max(Double::compare)
                .orElse(0.);
        stat.setFiftyTowWeekHigh(df.format(maxHigh));
        stat.setFiftyTowWeekLow(df.format(minLow));
        stat.setTwoHundredDaysMovingAverage(calcMovingAverage(sortedList, symbol, 200));
        stat.setFiftyDaysMovingAverage(calcMovingAverage(sortedList,symbol, 50));
    }

    public Statistics getStatisticsFromYahoo(Symbol symbol) throws JsonProcessingException {
        Map<String, String> receivedData = getter.getDataForStatistics(symbol);
        System.out.println(receivedData);
        receivedData.forEach((str1, str2) -> {
        String str = str1.replace(" ","").replace("/", "To");
        });
        stat = mapper.map(receivedData, Statistics.class);
        System.out.println(receivedData);
        stat.setSymbol(symbol);
        return stat;
    }

        public Statistics getNewStatistics(Symbol symbol) throws JsonProcessingException {
           Statistics stat = getStatisticsFromYahoo(symbol);
           calcStatistics(symbol);
           return stat;
        }

        public StatisticsDto getStatisticsDto (String ticker) throws JsonProcessingException {
            Symbol symbol = symbolService.getSymbol(ticker);
            Statistics stat;
            StatisticsDto dto =  new StatisticsDto();
            if(statisticsRepository.existsBySymbol(symbol)) {
                stat = statisticsRepository.findBySymbol(symbol).get();
            }else{
                stat = getNewStatistics(symbol);
                statisticsRepository.save(stat);
            }
            dto.setValuationMeasures(mapper.map(stat, ValuationMeasures.class));
            dto.setStockPriceHistory(mapper.map(stat, StockPriceHistory.class));
            dto.setBalanceSheet(mapper.map(stat, com.stockanalytics.dto.statistcs.BalanceSheet.class));
            dto.setShareStatistics(mapper.map(stat, ShareStatistics.class));
            dto.setCashFlowStatement(mapper.map(stat, com.stockanalytics.dto.statistcs.CashFlowStatement.class));
            dto.setDividendsAndSplits(mapper.map(stat, com.stockanalytics.dto.statistcs.DividendsAndSplits.class));
            dto.setIncomeStatement(mapper.map(stat, com.stockanalytics.dto.statistcs.IncomeStatement.class));
            dto.setFiscalYear(mapper.map(stat, com.stockanalytics.dto.statistcs.FiscalYear.class));
            dto.setProfitability(mapper.map(stat, Profitability.class));
            return dto;
        }
    }

