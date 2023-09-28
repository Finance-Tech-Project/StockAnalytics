package com.stockanalytics.portfolio.service;
import java.time.LocalDate;
import java.util.List;


import com.stockanalytics.portfolio.dto.PortfolioDto;
import com.stockanalytics.portfolio.dto.StockDto;
import com.stockanalytics.portfolio.model.Portfolio;

public  interface PortfolioService {
      PortfolioDto createPortfolio(PortfolioDto portfolioDto);
         StockDto addStock(Long portfolioId,String symbol, int quantity);
         StockDto removeStock(Long portfolioId,String symbol, int quantity);
         double calculatePortfolioValue(LocalDate date,Long portfolioId);

         double comparePerformance(LocalDate dateFrom, LocalDate dateTo, String benchmarkSymbol);

    List<PortfolioDto>  getPortfolios(String userName);

    List<PortfolioDto> getAllPortfolios();

//    boolean isTickerValidOnDate(String symbol, LocalDate date);
}
