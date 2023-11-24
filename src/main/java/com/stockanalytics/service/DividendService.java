package com.stockanalytics.service;


import com.stockanalytics.dao.DividendRepository;
import com.stockanalytics.dao.SymbolRepository;
import com.stockanalytics.model.Dividend;
import com.stockanalytics.model.Symbol;
import com.stockanalytics.util.HTMLParser;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Repository
public class DividendService {
    final SymbolRepository symbolRepository;
    final DividendRepository dividendRepository;
    private final ModelMapper mapper;
    HTMLParser parser = new HTMLParser();

    @Transactional
    public List<Dividend> getData(Symbol symbol, LocalDate dateFrom, LocalDate dateTo) throws IOException {
        List<Dividend> listDiv = new ArrayList<>();
        if (symbol.getHasDividends()== -1)
            return listDiv;
        if (symbol.getHasDividends() == 0) {
             listDiv = parser.getDividend(symbol);
            if (listDiv.size() == 0) {
                symbol.setHasDividends(-1);
                return listDiv;
            } else {
                dividendRepository.saveAll(listDiv);
                symbol.setHasDividends(1);
            }
        }
        return  dividendRepository.findAllBySymbolAndDateBetween(symbol, dateFrom, dateTo);
    }
}
