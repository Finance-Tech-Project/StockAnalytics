package com.stockanalytics.util;

import com.stockanalytics.dao.StockQuoteRepository;
import com.stockanalytics.model.Symbol;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.time.LocalDate;


@Component
@RequiredArgsConstructor
public class DateGetter {

    public LocalDate getMaxDateFromStockQuoteForSavingData(Symbol symbol, StockQuoteRepository stockQuoteRepository) {
        if (stockQuoteRepository.getMaxDateBySymbol(symbol) == null) {
            return LocalDate.of(2001, 1, 1);
        } else if (stockQuoteRepository.getMaxDateBySymbol(symbol).isEqual(LocalDate.now())) {
            return LocalDate.now();
        } else {
            return stockQuoteRepository.getMaxDateBySymbol(symbol).plusDays(1);
        }
    }

    public boolean getFlagToSaveData(LocalDate maxDate) {
        return maxDate.isBefore(LocalDate.now());
    }
}
