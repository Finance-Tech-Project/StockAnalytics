package com.stockanalytics.util;

import com.stockanalytics.dto.SharpRatioDto;
import com.stockanalytics.model.BondYield;
import com.stockanalytics.model.StockQuote;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class SharpRatioCalculator {
    public static List<SharpRatioDto> calculateSharpRatios(List<StockQuote> quotes, List<BondYield> bondYields, LocalDate dateFrom, int years) {
        LocalDate dateTo = LocalDate.now().minusDays(1); // Вчерашняя дата
        List<SharpRatioDto> sharpRatios = new ArrayList<>();

        for (LocalDate date = dateFrom; !date.isAfter(dateTo); date = date.plusDays(1)) {
            double averageStockReturn = calculateAverageStockReturn(quotes, date, years);
            double riskFreeRate = getRiskFreeRate(bondYields, date);

            double sharpRatioValue = (averageStockReturn - riskFreeRate) / calculateStandardDeviation(quotes, date, years);
            sharpRatios.add(new SharpRatioDto(date, sharpRatioValue));

        }

        return sharpRatios;
    }


    public static double calculateAverageStockReturn(List<StockQuote> quotes, LocalDate date, int years) {
        double finalPrice = 0.;
        double initialPrice = 0.;
        for (StockQuote quote : quotes) {
            if (quote.getDate().equals(date)) {
                finalPrice = quote.getClose();
            }
            if (quote.getDate().equals(date.minusYears(1)) || quote.getDate().isAfter(date.minusYears(1))) {
                initialPrice = quote.getClose();
            }
        }
        return (finalPrice - initialPrice) / initialPrice;
    }

    private static double getRiskFreeRate(List<BondYield> bondYields, LocalDate date) {
        double riskFreeRate = 0;
        for (BondYield bondYield : bondYields) {
            if (bondYield.getDate().equals(date) || bondYield.getDate().isAfter(date)) {
                riskFreeRate = bondYield.getYield();
            }
        }return riskFreeRate;
    }

    private static double calculateStandardDeviation(List<StockQuote> quotes, LocalDate date, int years) {
        // Рассчитайте стандартное отклонение доходности акций за последние years лет
        // Реализуйте этот метод сами
        return 0.0;
    }
}

