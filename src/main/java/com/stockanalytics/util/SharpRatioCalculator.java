package com.stockanalytics.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SharpRatioCalculator {
//    private final StockQuoteService stockQuoteService;
//    private final BondYieldRepository bondYieldRepository;
//
//
//
//public List<SharpRatioDto> calculateSharpRatios(LocalDate dateFrom, LocalDate dateTo, Symbol symbol) {
//    List<StockQuoteDto> stockQuotes = stockQuoteService.getQuotesByPeriod(dateFrom, dateTo, symbol);
//    List<BondYield> bondYields = bondYieldRepository.findAllByDateBetween(dateFrom, dateTo);
//    List<SharpRatioDto> sharpRatios = new ArrayList<>();
//    int years = 1; // Расчетный период - один год (252 рабочих дня)
//
//    for (LocalDate currentDate = dateFrom; !currentDate.isAfter(dateTo); currentDate = currentDate.plusDays(1)) {
//        if (currentDate.isBefore(dateFrom.plusYears(years))) {
//            continue; // Пропускаем дни, когда окно еще не набралось
//        }
//
//        LocalDate windowStartDate = currentDate.minusYears(years);
//
//        double averageStockReturn = calculateAverageReturn(stockQuotes, windowStartDate, currentDate);
//        double averageRiskFreeReturn = calculateAverageRiskFreeReturn(bondYields, windowStartDate, currentDate);
//        double volatility = calculateVolatility(stockQuotes, windowStartDate, currentDate);
//
//        if (volatility > 0) {
//            double sharpRatio = (averageStockReturn - averageRiskFreeReturn) / volatility;
//            sharpRatios.add(new SharpRatioDto(currentDate, sharpRatio));
//        }
//    }
//
//    return sharpRatios;
//}
//
//    private static double calculateAverageReturn(List<StockQuoteDto> stockQuotes, LocalDate startDate, LocalDate endDate) {
//        double sumReturns = 0;
//        int countReturns = 0;
//
//        for (StockQuoteDto quote : stockQuotes) {
//            if (!quote.getDate().isBefore(startDate) && !quote.getDate().isAfter(endDate)) {
//                sumReturns += (quote.getClose()/ stockQuotes.get(stockQuotes.indexOf(quote) - 1).getClose() - 1);
//                countReturns++;
//            }
//        }
//
//        return countReturns > 0 ? sumReturns / countReturns : 0;
//    }
//    private static double calculateAverageRiskFreeReturn(List<BondYield> bondYields, LocalDate startDate, LocalDate endDate) {
//        double sumYields = 0;
//        int countYields = 0;
//
//        for (BondYield yield : bondYields) {
//            if (!yield.getDate().isBefore(startDate) && !yield.getDate().isAfter(endDate)) {
//                sumYields += yield.getYield();
//                countYields++;
//            }
//        }
//
//        return countYields > 0 ? sumYields / countYields : 0;
//    }
//
//    private static double calculateVolatility(List<StockQuoteDto> stockQuotes, LocalDate startDate, LocalDate endDate) {
//        double sumSquaredReturns = 0;
//        int countReturns = 0;
//
//        for (StockQuoteDto quote : stockQuotes) {
//            if (!quote.getDate().isBefore(startDate) && !quote.getDate().isAfter(endDate)) {
//                double dailyReturn = quote.getClose() / stockQuotes.get(stockQuotes.indexOf(quote) - 1).getClose() - 1;
//                sumSquaredReturns += Math.pow(dailyReturn, 2);
//                countReturns++;
//            }
//        }
//
//        double variance = countReturns > 0 ? sumSquaredReturns / countReturns : 0;
//        return Math.sqrt(variance);
//    }
}

