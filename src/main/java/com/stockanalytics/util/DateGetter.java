package com.stockanalytics.util;

import com.stockanalytics.dao.StockQuoteRepository;
import com.stockanalytics.dto.StockQuoteDto;
import com.stockanalytics.model.Symbol;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DateGetter {
    private final DataGetter dataGetter;

    public LocalDate getMaxDateFromStockQuoteForSavingData(
            Symbol symbol,
            StockQuoteRepository stockQuoteRepository,
            LocalDate dateFrom
    ) {
        List<StockQuoteDto> quotesFromParser = dataGetter.getHistoryStockQuotesInDateRange(symbol, dateFrom);
        LocalDate maxDateFromParser = quotesFromParser.get(quotesFromParser.size() - 1).getDate();

        if (stockQuoteRepository.getMaxDateBySymbol(symbol) == null) {
            return LocalDate.of(2001, 1, 1);
        } else if (stockQuoteRepository.getMaxDateBySymbol(symbol).isEqual(LocalDate.now())
                || stockQuoteRepository.getMaxDateBySymbol(symbol).isEqual(maxDateFromParser)) {
            return LocalDate.now();
        } else {
            return stockQuoteRepository.getMaxDateBySymbol(symbol).plusDays(1);
        }
    }

    public boolean getFlagToSaveData(LocalDate maxDate) {
        return maxDate.isBefore(LocalDate.now());
    }
}
