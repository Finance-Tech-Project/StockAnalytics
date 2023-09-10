package com.stockanalytics.service;

import com.stockanalytics.dao.StockQuoteRepository;
import com.stockanalytics.dao.SymbolRepository;
import com.stockanalytics.dto.StatisticsDto;
import com.stockanalytics.dto.StockQuoteDto;
import com.stockanalytics.model.StockQuote;
import com.stockanalytics.model.StockQuoteId;
import com.stockanalytics.model.Symbol;
import com.stockanalytics.util.DataGetter;
import com.stockanalytics.util.StockQuoteProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Repository
public class StockQuoteService {

final StockQuoteRepository stockQuoteRepository;
    final SymbolRepository symbolRepository;
    final StockQuoteProcessor processor = new StockQuoteProcessor();
    DataGetter getter = new DataGetter();

    @Transactional
    public List<List<StockQuoteDto>> getData(LocalDate dateFrom, LocalDate dateTo, Symbol symbol) {
        if (symbol.getStatus() != 0) {
            return getQuotesByPeriod(dateFrom, dateTo, symbol);
        } else {
            List<StockQuoteDto> quotes = getter.getAllHistoryStockQuotes(symbol);
            List<StockQuote> stockQuotes = new ArrayList<>();
            for (StockQuoteDto quote : quotes) {
                StockQuoteId id = new StockQuoteId(quote.getDate(), symbol);
                StockQuote stockQuote = new StockQuote(id, quote.getOpen(), quote.getHigh(), quote.getLow(), quote.getClose(), quote.getVolume());
                stockQuoteRepository.save(stockQuote);
                stockQuotes.add(stockQuote);
            }
            List<StockQuoteDto> result = new ArrayList<>();
            for (StockQuoteDto quote : quotes) {
                if (quote.getDate().isBefore(dateTo) && quote.getDate().isAfter(dateFrom))
                    result.add(quote);
            }
            return processor.getAllQuoteLists(result, dateFrom, dateTo);
        }
    }

    public List<List<StockQuoteDto>> getQuotesByPeriod(LocalDate dateFrom, LocalDate dateTo, Symbol symbol) {
        List<StockQuote> quotes = stockQuoteRepository.findAllById_Symbol(symbol);
        LocalDate start = dateFrom.withMonth(1).withDayOfMonth(1);
        LocalDate end = dateTo.withMonth(12).withDayOfMonth(31);
        List<StockQuoteDto> list = quotes.stream()
                .filter(quote -> quote.getId().getDate().isAfter(start.minusDays(1)) && quote.getId().getDate().isBefore(end.plusDays(1)))
                .map(quote -> new StockQuoteDto(quote.getId().getDate(), quote.getOpen(), quote.getHigh(), quote.getLow(), quote.getClose(), quote.getVolume()))
                .collect(Collectors.toList());
        return processor.getAllQuoteLists(list, dateFrom, dateTo);
    }

    public List<StatisticsDto> getStatistics(Symbol symbol) throws IOException {
        Map<String, String> parameters = getter.getDataForStatistics(symbol);
        return  null;
    }
}
