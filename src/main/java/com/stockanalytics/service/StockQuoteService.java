package com.stockanalytics.service;

import com.stockanalytics.dao.StockQuoteRepository;
import com.stockanalytics.dto.StockQuoteDto;
import com.stockanalytics.model.StockQuote;
import com.stockanalytics.model.Symbol;
import com.stockanalytics.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Repository
public class StockQuoteService {
    private final StockQuoteRepository stockQuoteRepository;
    private final StockQuoteProcessor stockQuoteProcessor;
    private final DataGetter getter;
    private final SavingStockQuoteDataInDB savingStockQuoteDataInDB;
    private final DateGetter dateGetter;

    public List<StockQuoteDto> getData(Symbol symbol, LocalDate dateFrom, LocalDate dateTo) {

        LocalDate maxDate = dateGetter.getMaxDateFromStockQuoteForSavingData(symbol, stockQuoteRepository, dateFrom);
        if (dateGetter.getFlagToSaveData(maxDate)) {
           savingStockQuoteDataInDB.saveStockQuoteData(symbol, getter.getHistoryStockQuotesInDateRange(symbol, maxDate));
        }
        return getQuotesByPeriod(dateFrom, dateTo, symbol);
    }

    public List<List<StockQuoteDto>> getListsForChart(Symbol symbol, LocalDate dateFrom, LocalDate dateTo) {
        List<StockQuoteDto> list = getData(symbol, dateFrom, dateTo);
        return stockQuoteProcessor.getAllQuoteLists(list, dateFrom, dateTo);
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
}
