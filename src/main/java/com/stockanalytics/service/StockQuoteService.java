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
import java.text.DecimalFormat;
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
    DecimalFormat df = new DecimalFormat("#.##");

    private double round (double number){
        String str = df.format(number).replace(",", ".");
        return Double.parseDouble(str);
    }

    @Transactional
    public List<StockQuoteDto> getData(Symbol symbol,LocalDate dateFrom, LocalDate dateTo) {
        if (symbol.getStatus() != 0) {
            return getQuotesByPeriod(dateFrom, dateTo, symbol);
        } else {
            List<StockQuoteDto> quotes = getter.getAllHistoryStockQuotes(symbol);
            for (StockQuoteDto quote : quotes) {
                StockQuoteId id = new StockQuoteId(quote.getDate(), symbol);
                StockQuote stockQuote = new StockQuote(id, round(quote.getOpen()), round(quote.getHigh()), round(quote.getLow()), round(quote.getClose()), quote.getVolume());
                stockQuoteRepository.save(stockQuote);
            }
            List<StockQuoteDto> result = new ArrayList<>();
            for (StockQuoteDto quote : quotes) {
                if (quote.getDate().isBefore(dateTo) && quote.getDate().isAfter(dateFrom)){
                    quote.setOpen(round(quote.getOpen()));
                    quote.setHigh(round(quote.getHigh()));
                    quote.setLow(round(quote.getLow()));
                    quote.setClose(round(quote.getClose()));
                }
            result.add(quote);
            }
            return result;
        }
    }
    public List<List<StockQuoteDto>> getListsForChart (Symbol symbol, LocalDate dateFrom, LocalDate dateTo){
        List<StockQuoteDto> list = getData(symbol, dateFrom, dateTo);
        return processor.getAllQuoteLists(list,dateFrom, dateTo);
    }


    public List<StockQuoteDto> getQuotesByPeriod(LocalDate dateFrom, LocalDate dateTo, Symbol symbol) {
        List<StockQuote> quotes = stockQuoteRepository.findAllById_Symbol(symbol);
        LocalDate start = dateFrom.withMonth(1).withDayOfMonth(1);
        LocalDate end = dateTo.withMonth(12).withDayOfMonth(31);
        return quotes.stream()
                .filter(quote -> quote.getId().getDate().isAfter(start.minusDays(1)) && quote.getId().getDate().isBefore(end.plusDays(1)))
                .map(quote -> new StockQuoteDto(quote.getId().getDate(), quote.getOpen(), quote.getHigh(), quote.getLow(), quote.getClose(), quote.getVolume()))
                .collect(Collectors.toList());
    }

    public List<StatisticsDto> getStatistics(Symbol symbol) throws IOException {
        Map<String, String> parameters = getter.getDataForStatisticsFromYahoo(symbol);
        return  null;
    }
}
