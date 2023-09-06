package org.stockanalytics.util;

import lombok.NoArgsConstructor;
import org.stockanalytics.dto.StockQuoteDto;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@NoArgsConstructor
public class StockQuoteProcessor {

    public List<StockQuoteDto> getDailyStockQuotes(List<StockQuoteDto> quotes, LocalDate dateFrom, LocalDate dateTo) {
        return quotes.stream()
                .filter(quote -> quote.getDate().isAfter(dateFrom.minusDays(1)) && quote.getDate().isBefore(dateTo.plusDays(1)))
                .sorted(Comparator.comparing(StockQuoteDto::getDate))
                .collect(Collectors.toList());
    }
    public List<StockQuoteDto> getWeeklyStockQuotes(List<StockQuoteDto> quotes, LocalDate dateFrom, LocalDate dateTo) {
        LocalDate start = dateFrom.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate end = dateTo.with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));
        //                    LocalDate weekStartingDate = entry.getKey();
         List<StockQuoteDto> res =quotes.stream()
                .filter(quote -> quote.getDate().isAfter(start.minusDays(1)) && quote.getDate().isBefore(end.plusDays(1)))
                .collect(Collectors.groupingBy(this::getWeekStartingDate))
                .values().stream()
                .map(this::calculateWeeklyQuote)
                .sorted(Comparator.comparing(StockQuoteDto::getDate))
                .collect(Collectors.toList());
        System.out.println(res);
        return res;
    }

    private LocalDate getWeekStartingDate(StockQuoteDto quote) {
        return quote.getDate().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }

    public List<StockQuoteDto> getMonthlyStockQuotes(List<StockQuoteDto> quotes, LocalDate dateFrom, LocalDate dateTo) {
        LocalDate start = YearMonth.from(dateFrom).atDay(1);
        LocalDate end = dateTo.withDayOfMonth(dateTo.getMonth().length(dateTo.isLeapYear()));
        return quotes.stream()
               .filter(quote -> quote.getDate().isAfter(start.minusDays(1)) && quote.getDate().isBefore(end.plusDays(1)))
               .collect(Collectors.groupingBy(quote -> YearMonth.from(quote.getDate())))
               .values().stream()
               .map(this::calculateMonthlyQuote)
               .sorted(Comparator.comparing(StockQuoteDto::getDate))
               .collect(Collectors.toList());
    }

    public List<StockQuoteDto> getYearlyStockQuotes(List<StockQuoteDto> quotes, LocalDate dateFrom, LocalDate dateTo) {
        LocalDate start = dateFrom.withMonth(1).withDayOfMonth(1);
        LocalDate end = dateTo.withMonth(12).withDayOfMonth(31);
        return quotes.stream()
                .filter(quote -> quote.getDate().isAfter(start.minusDays(1)) && quote.getDate().isBefore(end.plusDays(1)))
                .collect(Collectors.groupingBy(quote -> Year.from(quote.getDate())))
                .values().stream()
                .map(this::calculateYearlyQuote)
                .sorted(Comparator.comparing(StockQuoteDto::getDate))
                .collect(Collectors.toList());
    }

    private StockQuoteDto calculateWeeklyQuote(List<StockQuoteDto> weekQuotes) {
        return calculateQuote( weekQuotes, weekQuotes.get(0).getDate());
    }

    private StockQuoteDto calculateMonthlyQuote( List<StockQuoteDto> monthQuotes) {
        return calculateQuote(monthQuotes,monthQuotes.get(0).getDate());
    }

    private StockQuoteDto calculateYearlyQuote(List<StockQuoteDto> yearQuotes) {
        return calculateQuote(yearQuotes, yearQuotes.get(0).getDate());
    }

    private StockQuoteDto calculateQuote(List<StockQuoteDto> quotes, LocalDate startingDate) {
        Double open = quotes.get(quotes.size() - 1).getOpen();
        Double close = quotes.get(0).getClose();
        Double high = quotes.stream().mapToDouble(StockQuoteDto::getHigh).max().orElse(0.0);
        Double low = quotes.stream().mapToDouble(StockQuoteDto::getLow).min().orElse(0.0);
        Long volume = quotes.stream().mapToLong(StockQuoteDto::getVolume).sum();
        return new StockQuoteDto(startingDate, open, high, low, close, volume);
    }

    public List<List<StockQuoteDto>> getAllQuoteLists(List<StockQuoteDto> quotes, LocalDate dateFrom, LocalDate dateTo) {
        List<StockQuoteDto> daylyQuotes = getDailyStockQuotes(quotes, dateFrom, dateTo);
        List<StockQuoteDto> weeklyQuotes = getWeeklyStockQuotes(quotes, dateFrom, dateTo);
        List<StockQuoteDto> monthlyQuotes = getMonthlyStockQuotes(quotes, dateFrom, dateTo);
        List<StockQuoteDto> yearlyQuotes = getYearlyStockQuotes(quotes, dateFrom, dateTo);

        return Arrays.asList(daylyQuotes, weeklyQuotes, monthlyQuotes, yearlyQuotes);
    }
}

