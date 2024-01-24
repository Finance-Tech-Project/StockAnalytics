package com.stockanalytics.portfolio.dto;

import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioValueDto {
    private LocalDate date;
    private double portfolioValue;
}
