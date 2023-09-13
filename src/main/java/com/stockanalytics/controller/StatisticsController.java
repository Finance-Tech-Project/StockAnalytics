package com.stockanalytics.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.stockanalytics.dto.StatisticsDto;
import com.stockanalytics.service.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StatisticsController {
    private final StatisticService statisticService;

    @PostMapping("/statistics")
    public StatisticsDto getStatistics(@RequestParam String ticker) throws JsonProcessingException {
        return statisticService.getStatisticsDto(ticker);
    }
}
