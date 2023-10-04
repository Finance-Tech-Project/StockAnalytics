package com.stockanalytics.util;

import com.stockanalytics.dto.SharpRatioDto;
import com.stockanalytics.model.BondYield;
import com.stockanalytics.model.StockQuote;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PortfolioMetrics {
    public static List<SharpRatioDto> getSharpRatio(List<StockQuote> stockQuotes, List<BondYield> bondYields, int years) {
        List<SharpRatioDto> sharpRatios = new ArrayList<>();
        LocalDate dateFrom = stockQuotes.get(0).getDate(); // Начальная дата

        for (int i = years * 365; i < stockQuotes.size(); i++) {
            LocalDate currentDate = stockQuotes.get(i).getDate();
            LocalDate startDate = currentDate.minusYears(years);
            double portfolioReturn = calculatePortfolioReturn(stockQuotes, bondYields, startDate, currentDate);
            double portfolioStdDev = calculatePortfolioStdDev(stockQuotes, bondYields, startDate, currentDate);

            if (portfolioStdDev != 0) {
                double sharpRatio = portfolioReturn / portfolioStdDev;
                sharpRatios.add(new SharpRatioDto(currentDate, sharpRatio));
            }
        }

        return sharpRatios;
    }

    private static double calculatePortfolioReturn(List<StockQuote> stockQuotes, List<BondYield> bondYields, LocalDate startDate, LocalDate endDate) {
        // Рассчитать доходность портфеля (например, среднюю доходность акций за период)
        // В этом примере, мы просто рассчитываем среднюю доходность акций за период
        double totalStockReturn = 0;
        int stockCount = 0;

        for (StockQuote quote : stockQuotes) {
            if (quote.getDate().isAfter(startDate) && quote.getDate().isBefore(endDate)) {
                totalStockReturn += quote.getClose();
                stockCount++;
            }
        }

        double averageStockReturn = totalStockReturn / stockCount;

        // Рассчитать доходность безрискового актива (например, среднюю доходность облигаций за период)
        // В этом примере, мы просто рассчитываем среднюю доходность облигаций за период
        double totalBondReturn = 0;
        int bondCount = 0;

        for (BondYield yield : bondYields) {
            if (yield.getDate().isAfter(startDate) && yield.getDate().isBefore(endDate)) {
                totalBondReturn += yield.getYield();
                bondCount++;
            }
        }

        double averageBondReturn = totalBondReturn / bondCount;
            return averageStockReturn - averageBondReturn;
    }

    private static double calculatePortfolioStdDev(List<StockQuote> stockQuotes, List<BondYield> bondYields, LocalDate startDate, LocalDate endDate) {
        // Рассчитать стандартное отклонение доходности портфеля
        // В этом примере, мы просто возвращаем фиксированное значение (для упрощения)
        return 0.1; // Замените на реальное стандартное отклонение
    }
}

