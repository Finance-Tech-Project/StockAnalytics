package com.stockanalytics.portfolio.dto;
import java.time.LocalDate;
import java.util.List;
import com.stockanalytics.portfolio.model.PortfolioStocks;
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
    private String portfolioName;
    private LocalDate portfolioDate;
    private List<PortfolioStocks> stocks;
}
