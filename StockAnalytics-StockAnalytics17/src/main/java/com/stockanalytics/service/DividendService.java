package com.stockanalytics.service;

import com.stockanalytics.dao.DividendRepository;
import com.stockanalytics.dao.SymbolRepository;
import com.stockanalytics.model.Dividend;
import com.stockanalytics.model.Symbol;
import com.stockanalytics.util.HTMLParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Repository
public class DividendService {
    final SymbolRepository symbolRepository;
    final DividendRepository dividendRepository;
    final HTMLParser parser = new HTMLParser();

    @Transactional
    public List<Dividend> getData(Symbol symbol, LocalDate dateFrom, LocalDate dateTo) {
        List<Dividend> listDiv = new ArrayList<>();
        if (symbol.getHasDividends() == 1)
            return listDiv;
        if (symbol.getHasDividends() == 0) {
            listDiv = parser.getDividend(symbol);
            dividendRepository.saveAll(listDiv);
        }
        return dividendRepository.findAllBySymbolAndDateBetween(symbol, dateFrom, dateTo);
    }

    public double DividendYieldPerPeriod(Symbol symbol, LocalDate dateFrom, LocalDate dateTo) {
        return getData(symbol, dateFrom, dateTo).stream()
                .mapToDouble(Dividend::getDividendRate)
                .sum();
    }
}
