package org.stockanalytics.service;

import org.stockanalytics.dto.StockQuoteDto;

import java.util.List;

public interface StockQuoteService {

    List<List<StockQuoteDto>> findDatesInterval(String symbolName, String firstDate, String lastDate);

}
