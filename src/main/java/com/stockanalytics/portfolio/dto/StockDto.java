package com.stockanalytics.portfolio.dto;
import com.stockanalytics.portfolio.model.PortfolioStocks;
import lombok.*;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockDto {
    public  String portfolioName;
    private List<PortfolioStocks> stocks;
}
