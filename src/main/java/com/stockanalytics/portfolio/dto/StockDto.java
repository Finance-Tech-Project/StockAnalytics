package com.stockanalytics.portfolio.dto;

import lombok.*;

import java.util.Map;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockDto {
    private Map<String, Integer> stocks;
}
