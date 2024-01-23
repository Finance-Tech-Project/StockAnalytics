package com.stockanalytics.controller;


import com.stockanalytics.dto.AveragePriceByPeriodDto;
import com.stockanalytics.dto.IncomePercentByPeriodDto;
import com.stockanalytics.dto.SharpRatioDto;
import com.stockanalytics.dto.VolatilityDto;
import com.stockanalytics.service.AnaliticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/analytics")
public class AnalyticsController {
    private final AnaliticsService analiticsService;

    @GetMapping("/movAvg")
    public List<AveragePriceByPeriodDto> getMovingAverage(@RequestParam String dateFrom, @RequestParam String dateTo,
                                                          @RequestParam String ticker, @RequestParam int period) {
        LocalDate end = LocalDate.parse(dateTo);
        LocalDate start = LocalDate.parse(dateFrom);
        return analiticsService.getMovingAverage(start, end, ticker, period);
    }

    @GetMapping("/simpleIncome")
    public List<IncomePercentByPeriodDto> getSimpleIncome(@RequestParam String dateFrom, @RequestParam String dateTo,
                                                          @RequestParam String ticker, @RequestParam int years) {
        LocalDate end = LocalDate.parse(dateTo);
        LocalDate start = LocalDate.parse(dateFrom);
        return analiticsService.getSimpleIncome(start, end, ticker, years);
    }

    @GetMapping("/volatility")
    public List<VolatilityDto> getDataAboutVolatility(@RequestParam String dateFrom, @RequestParam String dateTo,
                                                      @RequestParam String ticker, @RequestParam int years) {
        LocalDate end = LocalDate.parse(dateTo);
        LocalDate start = LocalDate.parse(dateFrom);
        return analiticsService.getDataAboutVolatility(start, end, ticker, years);
    }

    @GetMapping("/sharpRatios")
    public List<SharpRatioDto> getSharpRatios(@RequestParam String dateFrom, @RequestParam String dateTo,
                                              @RequestParam String ticker, @RequestParam int years) {
        LocalDate end = LocalDate.parse(dateTo);
        LocalDate start = LocalDate.parse(dateFrom);
        return analiticsService.getSharpRatios(start, end, ticker, years);
    }
}
