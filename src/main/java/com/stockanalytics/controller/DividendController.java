package com.stockanalytics.controller;


import com.stockanalytics.model.Dividend;
import com.stockanalytics.model.Symbol;
import com.stockanalytics.service.DividendService;
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
public class DividendController {
    private final DividendService dividendService;
    private final SymbolService symbolService;

    @GetMapping("/dividends/history")
    public List<Dividend> getDividends(@RequestParam String dateFrom, @RequestParam String dateTo, @RequestParam String ticker) throws IOException {

        Symbol symbol = symbolService.getSymbol(ticker);
        LocalDate end = LocalDate.parse(dateTo);
        LocalDate start = LocalDate.parse(dateFrom);

        return dividendService.getData(symbol, start, end);
    }
}
