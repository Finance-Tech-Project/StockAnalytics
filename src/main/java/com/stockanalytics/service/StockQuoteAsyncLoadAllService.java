package com.stockanalytics.service;

import com.stockanalytics.dao.StockQuoteRepository;
import com.stockanalytics.dao.SymbolRepository;
import com.stockanalytics.dto.StockQuoteDto;
import com.stockanalytics.model.StockQuote;
import com.stockanalytics.model.StockQuoteId;
import com.stockanalytics.model.Symbol;
import com.stockanalytics.util.DataGetter;
import com.stockanalytics.util.QuoteDataRounding;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
@Getter
public class StockQuoteAsyncLoadAllService {
    final QuoteDataRounding quoteDataRounding;
    final StockQuoteRepository stockQuoteRepository;
    final DataGetter getter = new DataGetter();
    final SymbolRepository symbolRepository;

    @Async
    @Transactional
    public void loadAll(Symbol symbol) {
        List<StockQuoteDto> quotes = getter.getAllHistoryStockQuotes(symbol);
        symbol.setStatus(1);
        System.out.println("Start save");
        for (StockQuoteDto quote : quotes) {
            StockQuoteId id = new StockQuoteId(quote.getDate(), symbol);
            StockQuote stockQuote = new StockQuote(
                    id, quoteDataRounding.round(quote.getOpen()),
                    quoteDataRounding.round(quote.getHigh()),
                    quoteDataRounding.round(quote.getLow()),
                    quoteDataRounding.round(quote.getClose()),
                    quote.getVolume());
            stockQuoteRepository.save(stockQuote);
        }
        symbolRepository.save(symbol);
        System.out.println("End save");
    }
}
