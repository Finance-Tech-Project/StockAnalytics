package org.stockanalytics.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.stockanalytics.dao.StockQuoteRepository;
import org.stockanalytics.dao.SymbolRepository;
import org.stockanalytics.dto.StockQuoteDto;
import org.stockanalytics.model.StockQuote;
import org.stockanalytics.model.StockQuoteId;
import org.stockanalytics.model.Symbol;
import org.stockanalytics.util.DateGetter;
import org.stockanalytics.util.StockQuoteProcessor;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Repository
public class StockQuoteService implements StockQuoteServiceInterface {

final StockQuoteRepository stockQuoteRepository;
    final SymbolRepository symbolRepository;
    StockQuoteProcessor processor = new StockQuoteProcessor();
    DateGetter getter = new DateGetter();

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

}
