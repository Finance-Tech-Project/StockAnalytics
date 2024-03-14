package com.stockanalytics.controller;

import com.stockanalytics.dto.StockQuoteDto;
import com.stockanalytics.model.Symbol;
import com.stockanalytics.service.StockQuoteService;
import com.stockanalytics.service.SymbolService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StockQuoteController {
    private final StockQuoteService stockQuoteService;
    private  final SymbolService symbolService;

    @GetMapping("/quote/history")
    public List<List<StockQuoteDto>> getHistoryBySymbol (@RequestParam String dateFrom, @RequestParam String dateTo, @RequestParam String ticker ){
        Symbol symbol = symbolService.getSymbol(ticker);
        LocalDate end = LocalDate.parse(dateTo);
        LocalDate start = LocalDate.parse(dateFrom);
        return stockQuoteService.getListsForChart(symbol, start, end);
    }
}
