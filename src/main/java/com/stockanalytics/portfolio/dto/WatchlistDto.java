package com.stockanalytics.portfolio.dto;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WatchlistDto {
    String symbolName;
    String companyName;
    String exchange;
    String industryCategory;
    Double close;
    int hasDividends;
}
