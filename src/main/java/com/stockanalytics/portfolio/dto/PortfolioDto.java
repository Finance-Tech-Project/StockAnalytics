package com.stockanalytics.portfolio.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PortfolioDto {
    private String username;
    private List<String> stocks;
    private LocalDate portfolioDate;
    private Map<String, Integer> symbols;

   
}
