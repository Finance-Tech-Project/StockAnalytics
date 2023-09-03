package org.stockanalytics.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.stockanalytics.dto.StockQuoteDto;
import org.stockanalytics.service.StockQuoteService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StockQuoteController {

    private final StockQuoteService stockQuoteService;

    @GetMapping(path = "/ticker/daily/symbol/{symbolName}/{firstDate}/{lastDate}")
    public List<List<StockQuoteDto>> findDatesInterval(@PathVariable String symbolName,
                                                       @PathVariable String firstDate,
                                                       @PathVariable String lastDate) {
        return stockQuoteService.findDatesInterval(symbolName, firstDate, lastDate);
    }

}
