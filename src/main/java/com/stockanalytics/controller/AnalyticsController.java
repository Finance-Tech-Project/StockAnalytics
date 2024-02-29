package com.stockanalytics.controller;


import com.stockanalytics.dto.AveragePriceByPeriodDto;
import com.stockanalytics.dto.IncomePercentByPeriodDto;
import com.stockanalytics.dto.SharpRatioDto;
import com.stockanalytics.dto.VolatilityDto;
import com.stockanalytics.service.AnalyticsService;
import com.stockanalytics.service.IRRService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/analytics")
public class AnalyticsController {
    private final AnalyticsService analyticsService;
    private final IRRService iRRService;

    @GetMapping("/movAvg")
    public List<AveragePriceByPeriodDto> getMovingAverage(@RequestParam String dateFrom, @RequestParam String dateTo,
                                                          @RequestParam String ticker, @RequestParam int period) {
        LocalDate end = LocalDate.parse(dateTo);
        LocalDate start = LocalDate.parse(dateFrom);
        return analyticsService.getMovingAverage(start, end, ticker, period);
    }

    @GetMapping("/simpleIncome")
    public List<IncomePercentByPeriodDto> getSimpleIncome(@RequestParam String dateFrom, @RequestParam String dateTo,
                                                          @RequestParam String ticker, @RequestParam int years) {
        LocalDate end = LocalDate.parse(dateTo);
        LocalDate start = LocalDate.parse(dateFrom);
        return analyticsService.getSimpleIncome(start, end, ticker, years);
    }

    @GetMapping("/volatility")
    public List<VolatilityDto> getDataAboutVolatility(@RequestParam String dateFrom, @RequestParam String dateTo,
                                                      @RequestParam String ticker, @RequestParam int years) {
        LocalDate end = LocalDate.parse(dateTo);
        LocalDate start = LocalDate.parse(dateFrom);
        return analyticsService.getDataAboutVolatility(start, end, ticker, years);
    }

    @GetMapping("/sharpRatios")
    public List<SharpRatioDto> getSharpRatios(@RequestParam String dateFrom, @RequestParam String dateTo,
                                              @RequestParam String ticker, @RequestParam int years) {
        LocalDate end = LocalDate.parse(dateTo);
        LocalDate start = LocalDate.parse(dateFrom);
        return analyticsService.getSharpRatios(start, end, ticker, years);
    }

    @GetMapping("/IRR")
    public ResponseEntity<Double> getIRR(@RequestParam String dateFrom,
                                         @RequestParam String dateTo,
                                         @RequestParam Double startAmount,
                                         @RequestParam String ticker) {
        try {
            LocalDate start = LocalDate.parse(dateFrom);
            LocalDate end = LocalDate.parse(dateTo);

            Double iRR = iRRService.calculateHistoricalIRR(start, end, startAmount, ticker);

            // Return the calculated IRR
            return new ResponseEntity<>(iRR, HttpStatus.OK); // Replace with actual IRR calculation
        } catch (DateTimeParseException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
