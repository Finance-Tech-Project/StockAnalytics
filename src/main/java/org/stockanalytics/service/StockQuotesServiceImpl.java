package org.stockanalytics.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.stockanalytics.dao.StockQuoteRepository;
import org.stockanalytics.dao.SymbolRepository;
import org.stockanalytics.dto.StockQuoteDto;

import java.util.List;

@RequiredArgsConstructor
@Service
public class StockQuotesServiceImpl implements StockQuoteService {

    private final String HISTORY_API_URL = "https://query1.finance.yahoo.com/v7/finance/download/%s?period1=-631159200&period2=%s&interval=1d&events=history";

    private final SymbolRepository symbolRepository;
    private final StockQuoteRepository stockQuoteRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<List<StockQuoteDto>> findDatesInterval(String symbolName, String firstDate, String lastDate) {
        return null;
    }
}
