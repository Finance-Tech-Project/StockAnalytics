package com.stockanalytics.controller;


import com.stockanalytics.dto.StatisticsDto;
import com.stockanalytics.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class StatisticsController {
    private final StatisticsService statisticsService;

    @PostMapping("/statistics")
    public StatisticsDto getStatistics(@RequestParam String ticker) throws IOException, InterruptedException {
        return statisticsService.getStatisticsDto(ticker);
    }
}
