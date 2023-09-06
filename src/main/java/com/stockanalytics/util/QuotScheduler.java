package com.stockanalytics.util;


import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.stockanalytics.dao.StockQuoteRepository;
import com.stockanalytics.dao.SymbolRepository;
import com.stockanalytics.dto.StockQuoteDto;
import com.stockanalytics.model.StockQuote;
import com.stockanalytics.model.StockQuoteId;
import com.stockanalytics.model.Symbol;
import com.stockanalytics.service.StockQuoteService;

import java.time.LocalDate;
import java.util.List;


@Component
@RequiredArgsConstructor
public class QuotScheduler {
    final StockQuoteRepository stockQuoteRepository;
    final SymbolRepository symbolRepository;
    final StockQuoteService stockQuoteService;
    final DateGetter getter;


    @Scheduled(cron = "0 0 * * * 1-5") //
    public void updateData() {
        List<Symbol> sym = symbolRepository.findAllByStatusIsGreaterThan(0);
        for (Symbol symbol : sym){
            LocalDate lastDate= stockQuoteRepository.getQuotDatesList(symbol).get(0);
            List<StockQuoteDto> newDtos = getter.getHistoryStockQuotes(lastDate.plusDays(1), LocalDate.now(), symbol);
            for (StockQuoteDto dto : newDtos) {
                StockQuote q = new StockQuote(new StockQuoteId(dto.getDate(), symbol), dto.getOpen(), dto.getHigh(), dto.getLow(), dto.getClose(), dto.getVolume());
                stockQuoteRepository.save(q);
                System.out.println("Data for " + symbol.getName() + " updated for " + q.getDate());
            }
        }
    }
}