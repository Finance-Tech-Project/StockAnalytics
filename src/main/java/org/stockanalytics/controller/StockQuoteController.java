package org.stockanalytics.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.stockanalytics.dto.StockQuoteDto;
import org.stockanalytics.model.Symbol;
import org.stockanalytics.service.StockQuoteService;
import org.stockanalytics.service.StockQuoteServiceInterface;
import org.stockanalytics.service.SymbolService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StockQuoteController {

    private final StockQuoteService stockQuoteService;
    private  final SymbolService symbolService;

    @GetMapping(path = "/ticker/daily/symbol/{symbolName}/{firstDate}/{lastDate}")
    public List<List<StockQuoteDto>> findDatesInterval(@PathVariable String symbolName,
                                                       @PathVariable String firstDate,
                                                       @PathVariable String lastDate) {
        return stockQuoteService.findDatesInterval(symbolName, firstDate, lastDate);
    }
    @GetMapping("/quote/history")
    public List<List<StockQuoteDto>> getHistoryBySymbol (@RequestParam String dateFrom, @RequestParam String dateTo, @RequestParam String ticker ){

        Symbol symbol = symbolService.getSymbol(ticker);
        LocalDate end = LocalDate.parse(dateTo);
        LocalDate start = LocalDate.parse(dateFrom);
        return stockQuoteService.getData(start, end, symbol);
    }

}
