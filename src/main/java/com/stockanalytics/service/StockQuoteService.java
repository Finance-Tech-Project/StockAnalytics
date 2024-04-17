package com.stockanalytics.service;

import com.stockanalytics.dao.StockQuoteRepository;
import com.stockanalytics.dao.SymbolRepository;
import com.stockanalytics.dto.StatisticsDto;
import com.stockanalytics.dto.StockQuoteDto;
import com.stockanalytics.model.StockQuote;
import com.stockanalytics.model.Symbol;
import com.stockanalytics.util.DataGetter;
import com.stockanalytics.util.QuoteDataRounding;
import com.stockanalytics.util.StockQuoteProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Repository
public class StockQuoteService {
    final QuoteDataRounding quoteDataRounding;
    final StockQuoteRepository stockQuoteRepository;
    final SymbolRepository symbolRepository;
    final StockQuoteProcessor processor = new StockQuoteProcessor();
    final DataGetter getter = new DataGetter();
    final StockQuoteAsyncLoadAllService stockQuoteAsyncLoadAllService;

    public List<StockQuoteDto> getData(Symbol symbol, LocalDate dateFrom, LocalDate dateTo) {
        if (symbol.getStatus() == 0) {
            if (getQuotesByPeriod(dateFrom, dateTo, symbol).isEmpty()) {
                stockQuoteAsyncLoadAllService.loadAll(symbol);
            }
            return getter.getHistoryStockQuotesInDateRange(symbol, dateFrom);
        }
        return getQuotesByPeriod(dateFrom, dateTo, symbol);
    }

    public StockQuote getSingleDate(Symbol symbol, LocalDate date) {
        if (symbol.getStatus() == 0) {
            stockQuoteAsyncLoadAllService.loadAll(symbol);
        }
        return stockQuoteRepository.getBySymbolAndDate(symbol, date);
    }

    public List<StockQuote> getListByIdAndDateBetween(Symbol symbol, LocalDate dateFrom, LocalDate dateTo) {
        if (symbol.getStatus() == 0) {
            stockQuoteAsyncLoadAllService.loadAll(symbol);
        }
        return stockQuoteRepository.findAllByIdIdAndDateBetween(symbol, dateFrom, dateTo);
    }

    @Async
    public CompletableFuture<List<List<StockQuoteDto>>> getListsForChart(Symbol symbol, LocalDate dateFrom, LocalDate dateTo) {
        List<StockQuoteDto> list = getData(symbol, dateFrom, dateTo);
        return CompletableFuture.completedFuture(processor.getAllQuoteLists(list, dateFrom, dateTo)) ;
    }

    public List<StockQuoteDto> getQuotesByPeriod(LocalDate dateFrom, LocalDate dateTo, Symbol symbol) {
        List<StockQuote> quotes = stockQuoteRepository.findAllById_Symbol(symbol);
        LocalDate start = dateFrom.withMonth(1).withDayOfMonth(1);
        LocalDate end = dateTo.withMonth(12).withDayOfMonth(31);
        return quotes.stream()
                .filter(quote ->
                        quote.getId().getDate().isAfter(start.minusDays(1))
                                && quote.getId().getDate().isBefore(end.plusDays(1)))
                .map(quote -> new StockQuoteDto(
                        quote.getId().getDate(),
                        quote.getOpen(), quote.getHigh(),
                        quote.getLow(),
                        quote.getClose(),
                        quote.getVolume()))
                .collect(Collectors.toList());
    }

    public List<StatisticsDto> getStatistics(Symbol symbol) {
        Map<String, String> parameters = getter.getDataForStatisticsFromYahoo(symbol);
        return null;
    }
}
