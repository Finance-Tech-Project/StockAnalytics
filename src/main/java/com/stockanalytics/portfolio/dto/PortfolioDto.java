package com.stockanalytics.portfolio.dto;

import java.time.LocalDate;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PortfolioDto {
    private String userLogin;
    private LocalDate portfolioDate;
    private Map<String, Integer> stocks;
}
