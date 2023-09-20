package com.stockanalytics.util;

import com.stockanalytics.dao.StockQuoteRepository;
import com.stockanalytics.dto.AveragePriceByPeriodDto;
import com.stockanalytics.dto.IncomePercentByPeriodDto;
import com.stockanalytics.model.StockQuote;
import com.stockanalytics.model.Symbol;
import lombok.RequiredArgsConstructor;
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

    private Double calcAvgPriceByPeriod(List<StockQuote> list) {
        return list.stream().map(StockQuote::getClose).mapToDouble(Double::doubleValue).average().orElse(0);
    }

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

    public List<IncomePercentByPeriodDto> calcSimpleIncomeList(LocalDate dateFrom, LocalDate dateTo, Symbol symbol, int days) {
        List<IncomePercentByPeriodDto> incomeList = new ArrayList<>();
        List<StockQuote> quotes = stockQuoteRepository.findAllByIdIdAndDateBetween(symbol, dateFrom.minusDays(days), dateTo).stream()
                .sorted(Comparator.comparing(StockQuote::getDate))
                .collect(Collectors.toList());

        for (StockQuote quote : quotes) {
            LocalDate currentDate = quote.getDate();

            if (currentDate.isAfter(dateFrom) && !currentDate.isAfter(dateTo)) {
                LocalDate previousDate = currentDate.minusDays(days);

                StockQuote previousQuot = null;
                for (StockQuote q : quotes) {
                    if (q.getDate().isEqual(previousDate) || q.getDate().isAfter(previousDate)) {
                        previousQuot = q;
                        break;
                    }
                }

                if (previousQuot != null) {
                    double income = quote.getClose() - previousQuot.getClose();
                    BigDecimal incomePercent = new BigDecimal(100 * income / previousQuot.getClose()).setScale(2, RoundingMode.HALF_UP);
                    incomeList.add(new IncomePercentByPeriodDto(currentDate, incomePercent));
                }
            }
        }
        return incomeList;
    }

}

