package com.stockanalytics.util;


import com.stockanalytics.dao.StockQuoteRepository;
import com.stockanalytics.dao.SymbolRepository;
import com.stockanalytics.dto.StockQuoteDto;
import com.stockanalytics.model.StockQuote;
import com.stockanalytics.model.StockQuoteId;
import com.stockanalytics.model.Symbol;
import com.stockanalytics.service.StatisticsService;
import com.stockanalytics.service.StockQuoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@Component
@RequiredArgsConstructor
public class QuotScheduler {
    final StockQuoteRepository stockQuoteRepository;
    final SymbolRepository symbolRepository;
    final StockQuoteService stockQuoteService;
    final StatisticsService statisticsService;
    final DataGetter getter;


    @Scheduled(cron = "0 0 4 * * 1-5") //
    public void updateHistoryData() {
        List<Symbol> sym = symbolRepository.findAllByStatusIsGreaterThan(0);
        for (Symbol symbol : sym){
            LocalDate lastDate= stockQuoteRepository.getQuotDatesList(symbol).get(0);
            List<StockQuoteDto> newDtos = getter.getHistoryStockQuotes(lastDate.plusDays(1), LocalDate.now(), symbol);
            for (StockQuoteDto dto : newDtos) {
                StockQuote q = new StockQuote(new StockQuoteId(dto.getDate(), symbol), dto.getOpen(), dto.getHigh(), dto.getLow(), dto.getClose(), dto.getVolume());
                stockQuoteRepository.save(q);
                System.out.println("Data for " + symbol.getName() + " updated for " + q.getDate() + " at " + LocalTime.now());
            }
        }
    }

    @Scheduled(cron = "0 0 7 * * 6")
    public void updateStatistics() throws IOException, InterruptedException {
        statisticsService.updateStatistics();
        System.out.println("Statistics  updated  " + LocalDate.now() + " at " + LocalTime.now());
    }

}
