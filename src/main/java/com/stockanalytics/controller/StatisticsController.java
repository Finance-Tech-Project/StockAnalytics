package com.stockanalytics.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.stockanalytics.dto.StatisticsDto;
import com.stockanalytics.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StatisticsController {
    private final StatisticsService statisticsService;

    @PostMapping("/statistics")
    public StatisticsDto getStatistics(@RequestParam String ticker) throws JsonProcessingException {
        return statisticsService.getStatisticsDto(ticker);
    }
}
