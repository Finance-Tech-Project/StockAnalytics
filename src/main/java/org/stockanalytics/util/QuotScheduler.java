package org.stockanalytics.util;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.stockanalytics.dao.StockQuoteRepository;
import org.stockanalytics.dao.SymbolRepository;
import org.stockanalytics.dto.StockQuoteDto;
import org.stockanalytics.model.StockQuote;
import org.stockanalytics.model.StockQuoteId;
import org.stockanalytics.model.Symbol;
import org.stockanalytics.service.StockQuoteService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;


@Component
@RequiredArgsConstructor
public class QuotScheduler {
    final StockQuoteRepository stockQuoteRepository;
    final SymbolRepository symbolRepository;
    final StockQuoteService stockQuoteService;
    final DateGetter getter;

    @Scheduled(cron = "0 0 22 * * 1-5") // Расписание в формате cron
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