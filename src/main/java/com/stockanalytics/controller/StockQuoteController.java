package com.stockanalytics.controller;

import com.stockanalytics.dto.StockQuoteDto;
import com.stockanalytics.model.Symbol;
import com.stockanalytics.service.StockQuoteService;
import com.stockanalytics.service.SymbolService;
import com.stockanalytics.util.DataGetter;
import com.stockanalytics.util.StockQuoteProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
public class StockQuoteController {
    private final StockQuoteService stockQuoteService;
    private  final SymbolService symbolService;
    final StockQuoteProcessor processor = new StockQuoteProcessor();
    final DataGetter getter = new DataGetter();

    @GetMapping("/quote/history")
    public List<List<StockQuoteDto>> getHistoryBySymbol (
            @RequestParam String dateFrom,
            @RequestParam String dateTo,
            @RequestParam String ticker
    ) throws ExecutionException, InterruptedException {
        Symbol symbol = symbolService.getSymbol(ticker);
        LocalDate end = LocalDate.parse(dateTo);
        LocalDate start = LocalDate.parse(dateFrom);


        if (symbol.getStatus() == 0) {
            stockQuoteService.loadAll(symbol);
            System.out.println("In Save");
            return processor.getAllQuoteLists(getter.getHistoryStockQuotesInDateRange(symbol, start), start, end);
        }

        return stockQuoteService.getListsForChart(symbol,start, end ).get();
    }
}
