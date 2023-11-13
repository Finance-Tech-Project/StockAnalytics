package com.stockanalytics.util;

import com.stockanalytics.dao.BondYieldRepository;
import com.stockanalytics.dao.StockQuoteRepository;
import com.stockanalytics.dto.*;
import com.stockanalytics.model.BondYield;
import com.stockanalytics.model.StockQuote;
import com.stockanalytics.model.Symbol;
import lombok.RequiredArgsConstructor;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor

public class Calculator {
    private final StockQuoteRepository stockQuoteRepository;
    private final BondYieldRepository bondYieldRepository;

    public List<AveragePriceByPeriodDto> calcMovingAvg(LocalDate dateFrom, LocalDate dateTo, Symbol symbol, int days) {
        List<AveragePriceByPeriodDto> movingAverage = new ArrayList<>();
        List<StockQuote> quotes = stockQuoteRepository.findAllByIdIdAndDateBetween(symbol, dateFrom.minusDays(days), dateTo).stream()
                .sorted(Comparator.comparing(StockQuote::getDate))
                .collect(Collectors.toList());
        for (StockQuote quote : quotes) {
            LocalDate currentDate = quote.getDate();
            if (currentDate.isBefore(dateFrom) || currentDate.isAfter(dateTo)) {
                continue;
            }
            double sum = 0.0;
            int count = 0;
            for (StockQuote innerQuote : quotes) {
                LocalDate innerDate = innerQuote.getDate();
                if (!innerDate.isBefore(currentDate.minusDays(days)) && !innerDate.isAfter(currentDate)) {
                    sum += innerQuote.getClose();
                    count++;
                }
            }
            if (count > 0) {
                double average = sum / count;
                BigDecimal result = new BigDecimal(average).setScale(2, RoundingMode.HALF_UP);
                movingAverage.add(new AveragePriceByPeriodDto(currentDate, result));
            }
        }
        return movingAverage;
    }

    public List<IncomePercentByPeriodDto> calcSimpleIncomeList(LocalDate dateFrom, LocalDate dateTo, Symbol symbol, int years) {
        List<IncomePercentByPeriodDto> incomeList = new ArrayList<>();
        List<StockQuote> quotes = stockQuoteRepository.findAllByIdIdAndDateBetween(symbol, dateFrom.minusYears(years), dateTo).stream()
                .sorted(Comparator.comparing(StockQuote::getDate))
                .collect(Collectors.toList());

        for (StockQuote quote : quotes) {
            LocalDate currentDate = quote.getDate();
            if (currentDate.isAfter(dateFrom) && !currentDate.isAfter(dateTo)) {
                LocalDate startDate = currentDate.minusYears(years);

                StockQuote startQuot = null;
                for (StockQuote q : quotes) {
                    if (q.getDate().isEqual(startDate) || q.getDate().isAfter(startDate)) {
                        startQuot = q;
                        break;
                    }
                }

                if (startQuot != null) {
                    double income = quote.getClose() - startQuot.getClose();
                    BigDecimal incomePercent = new BigDecimal(100 * income / startQuot.getClose()).setScale(2, RoundingMode.HALF_UP);
                    incomeList.add(new IncomePercentByPeriodDto(currentDate, incomePercent));
                }
            }
        }
        return incomeList;
    }

    public List<VolatilityDto> calculateVolatility(LocalDate dateFrom, LocalDate dateTo, Symbol symbol, int days) {
        List<VolatilityDto> result = new ArrayList<>();
        List<StockQuote> quotes = stockQuoteRepository.findAllByIdIdAndDateBetween(symbol, dateFrom.minusDays(days+1), dateTo).stream()
                .sorted(Comparator.comparing(StockQuote::getDate))
                .collect(Collectors.toList());

        for (StockQuote quote : quotes) {
            LocalDate currentDate = quote.getDate();
            DescriptiveStatistics stats = new DescriptiveStatistics();
            if (currentDate.isAfter(dateFrom.minusDays(1)) && !currentDate.isAfter(dateTo.minusDays(1))) {
                LocalDate startDate = currentDate.minusDays(days);

                for (StockQuote q : quotes) {
                    if (q.getDate().isAfter(quote.getDate())) {
                        break;
                    }
                    if (q.getDate().isEqual(startDate) || q.getDate().isAfter(startDate)) {
                        stats.addValue(q.getClose());
                    }
                }
                if (stats.getN() > 0) {
                    double vol = stats.getStandardDeviation();
                    BigDecimal volatility = new BigDecimal(vol).setScale(2, RoundingMode.HALF_UP);
                    result.add(new VolatilityDto(quote.getDate(), volatility));
                }
            }
        }
        return result;
    }

    public List<SharpRatioDto> calculateSharpRatios(LocalDate dateFrom, LocalDate dateTo, Symbol symbol, int years) {

        List<StockQuote> quotes = stockQuoteRepository.findAllByIdIdAndDateBetween(symbol, dateFrom.minusYears(years), dateTo);
        List<BondYield> bondYields = bondYieldRepository.findBondYieldsBetweenDates(dateFrom, dateTo);
        List<SharpRatioDto> sharpRatios = new ArrayList<>();
        double stockReturn = 0.;
        double currentYield = 0.;
        double volatility = 0.;
        DescriptiveStatistics stats = new DescriptiveStatistics();

        for (StockQuote quote : quotes) {
            LocalDate currentDate = quote.getDate();
            if (currentDate.isAfter(dateFrom) && !currentDate.isAfter(dateTo)) {
                LocalDate startDate = currentDate.minusYears(years);
                StockQuote startQuot = null;
                for (StockQuote q : quotes) {
                    if (q.getDate().isEqual(startDate) || q.getDate().isAfter(startDate)) {
                        startQuot = q;
                        stats.addValue(q.getClose());
                        break;
                    }
                    for (BondYield yield : bondYields) {
                        if (yield.getDate().isEqual(startDate) || yield.getDate().isAfter(startDate)) {
                            currentYield = yield.getYield();
                        }
                    }
                }
                if (startQuot != null) {
                    double income = quote.getClose() - startQuot.getClose();
                    stockReturn = 100 * income / startQuot.getClose();
                }
                if (stats.getN() > 0) {
                    volatility = stats.getStandardDeviation();
                }
                if (volatility > 0) {
                    double sharpRatio = (stockReturn - currentYield) / volatility;
                    sharpRatios.add(new SharpRatioDto(currentDate, sharpRatio));
                }
            }

        }
        return sharpRatios;
    }
}


