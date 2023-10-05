package com.stockanalytics.service;

import com.stockanalytics.dto.AveragePriceByPeriodDto;
import com.stockanalytics.dto.IncomePercentByPeriodDto;
import com.stockanalytics.dto.VolatilityDto;
import com.stockanalytics.model.Symbol;
import com.stockanalytics.util.Calculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AnaliticsService {
    private final Calculator calculator;
    private final SymbolService symbolService;

    public List<AveragePriceByPeriodDto> getMovingAverage(LocalDate dateFrom, LocalDate dateTo, String ticker, int period) {
        Symbol symbol = symbolService.getSymbol(ticker);
        return  calculator.calcMovingAvg(dateFrom, dateTo,symbol, period);
    }

    public List<IncomePercentByPeriodDto> getSimpleIncome(LocalDate dateFrom, LocalDate dateTo, String ticker, int years) {
        Symbol symbol = symbolService.getSymbol(ticker);
        return calculator.calcSimpleIncomeList(dateFrom, dateTo, symbol, years);
    }

    public List<VolatilityDto> getDataAboutVolatility(LocalDate dateFrom, LocalDate dateTo, String ticker, int days) {
        Symbol symbol = symbolService.getSymbol(ticker);
        return calculator.calculateVolatility(dateFrom, dateTo, symbol, days);
    }
}
