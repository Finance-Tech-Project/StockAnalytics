package com.stockanalytics.controller;

import com.stockanalytics.dto.StatisticsDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.stockanalytics.service.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.stockanalytics.dto.StockQuoteDto;
import com.stockanalytics.model.Symbol;
import com.stockanalytics.service.StockQuoteService;
import com.stockanalytics.service.SymbolService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StockQuoteController {
    private final StockQuoteService stockQuoteService;
    private  final SymbolService symbolService;
    private final StatisticService statisticsService;

    @GetMapping("/quote/history")
    public List<List<StockQuoteDto>> getHistoryBySymbol (@RequestParam String dateFrom, @RequestParam String dateTo, @RequestParam String ticker ){
        Symbol symbol = symbolService.getSymbol(ticker);
        LocalDate end = LocalDate.parse(dateTo);
        LocalDate start = LocalDate.parse(dateFrom);
        return stockQuoteService.getData(start, end, symbol);
    }

    @GetMapping("/statistics")
    public StatisticsDto getStatistics(@RequestParam String ticker) throws JsonProcessingException {
        return statisticsService.getStatisticsDto(ticker);
    }

}
