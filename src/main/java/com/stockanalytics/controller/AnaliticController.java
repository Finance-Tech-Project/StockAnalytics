package com.stockanalytics.controller;


import com.stockanalytics.dto.AveragePriceByPeriodDto;
import com.stockanalytics.dto.IncomePercentByPeriodDto;
import com.stockanalytics.dto.VolatilityDto;
import com.stockanalytics.service.AnaliticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AnaliticController {
    private final AnaliticsService analiticsService;

    @GetMapping("/analitics/movAvg")
    public List<AveragePriceByPeriodDto> getMovingAverage(@RequestParam String dateFrom, @RequestParam String dateTo,
                                                          @RequestParam String ticker, @RequestParam int period){
        LocalDate end = LocalDate.parse(dateTo);
        LocalDate start = LocalDate.parse(dateFrom);
        return analiticsService.getMovingAverage(start, end, ticker, period);
    }

    @GetMapping("/analitics/simpleIncome")
    public List<IncomePercentByPeriodDto> getSimpleIncome(@RequestParam String dateFrom, @RequestParam String dateTo,
                                                           @RequestParam String ticker, @RequestParam int years){
        LocalDate end = LocalDate.parse(dateTo);
        LocalDate start = LocalDate.parse(dateFrom);
        return analiticsService.getSimpleIncome(start, end, ticker, years);
    }

    @GetMapping("/analitics/volatility")
    public List<VolatilityDto> getDataAboutVolatility(@RequestParam String dateFrom, @RequestParam String dateTo,
                                                      @RequestParam String ticker, @RequestParam int days){
        LocalDate end = LocalDate.parse(dateTo);
        LocalDate start = LocalDate.parse(dateFrom);
        return analiticsService.getDataAboutVolatility(start, end, ticker, days);
    }
}
