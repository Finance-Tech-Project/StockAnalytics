package com.stockanalytics.controller;

import com.stockanalytics.dto.StatisticsDto;
import com.stockanalytics.dto.StockQuoteDto;
import com.stockanalytics.model.Symbol;
import com.stockanalytics.service.StatisticsService;
import com.stockanalytics.service.StockQuoteService;
import com.stockanalytics.service.SymbolService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StockQuoteController {
    private final StockQuoteService stockQuoteService;
    private  final SymbolService symbolService;
    private final StatisticsService statisticsService;

    @GetMapping("/quote/history")
    public List<List<StockQuoteDto>> getHistoryBySymbol (@RequestParam String dateFrom, @RequestParam String dateTo, @RequestParam String ticker ){
        Symbol symbol = symbolService.getSymbol(ticker);
        LocalDate end = LocalDate.parse(dateTo);
        LocalDate start = LocalDate.parse(dateFrom);
        return stockQuoteService.getListsForChart(symbol,start, end );
    }

    @GetMapping("/statistics")
    public StatisticsDto getStatistics(@RequestParam String ticker) throws IOException, InterruptedException {
        return statisticsService.getStatisticsDto(ticker);
    }

}
