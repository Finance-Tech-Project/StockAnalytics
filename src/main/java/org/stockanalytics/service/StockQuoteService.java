package org.stockanalytics.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.stockanalytics.dao.StockQuoteRepository;
import org.stockanalytics.dao.SymbolRepository;
import org.stockanalytics.dto.StockQuoteDto;
import org.stockanalytics.model.StockQuote;
import org.stockanalytics.model.StockQuoteId;
import org.stockanalytics.model.Symbol;
import org.stockanalytics.util.HistoryDateGetter;
import org.stockanalytics.util.StockQuoteProcessor;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StockQuoteService implements StockQuoteServiceInterface {

//    private final String HISTORY_API_URL = "https://query1.finance.yahoo.com/v7/finance/download/%s?period1=-631159200&period2=%s&interval=1d&events=history";
final StockQuoteRepository stockQuoteRepository;
    final SymbolRepository symbolRepository;
    ModelMapper mapper = new ModelMapper();
    StockQuoteProcessor processor = new StockQuoteProcessor();
    HistoryDateGetter getter = new HistoryDateGetter();

//    @Override
//    public List<List<StockQuoteDto>> findDatesInterval(String symbolName, String firstDate, String lastDate) {
//        return null;
//    }
//    @Override
    @Transactional
    public List<List<StockQuoteDto>> getData(LocalDate dateFrom, LocalDate dateTo, Symbol symbol) {

        if (stockQuoteRepository.existsById_Symbol(symbol)) {
            return getQuotesByPeriodAndTicker(dateFrom, dateTo, symbol);
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
    public List<List<StockQuoteDto>> getQuotesByPeriodAndTicker(LocalDate dateFrom, LocalDate dateTo, Symbol symbol) {
        List<StockQuote> quotes = stockQuoteRepository.findAllById_Symbol(symbol);
        LocalDate start = dateFrom.withMonth(1).withDayOfMonth(1);
        LocalDate end = dateTo.withMonth(12).withDayOfMonth(31);
        List<StockQuoteDto> list = quotes.stream()
                .filter(quote -> quote.getId().getDate().isAfter(start.minusDays(1)) && quote.getId().getDate().isBefore(end.plusDays(1)))
                .map(quote -> new StockQuoteDto(quote.getId().getDate(), quote.getOpen(), quote.getHigh(), quote.getLow(), quote.getClose(), quote.getVolume()))
                .collect(Collectors.toList());
        return processor.getAllQuoteLists(list, dateFrom, dateTo);
    }

//    @Override
    public Symbol getSymbol(String ticker) {
        Symbol symbol = symbolRepository.findById(ticker).get();
        return symbol;
    }


    public List<List<StockQuoteDto>> findDatesInterval(String symbolName, String firstDate, String lastDate) {
        return null;
    }
}
