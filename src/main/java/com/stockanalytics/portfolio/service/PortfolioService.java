package com.stockanalytics.portfolio.service;
import java.time.LocalDate;
import java.util.List;


import com.stockanalytics.portfolio.dto.PortfolioDto;
import com.stockanalytics.portfolio.dto.StockDto;
import com.stockanalytics.portfolio.service.exeptions.SymbolNotFoundException;

public  interface PortfolioService {
      PortfolioDto createPortfolio(PortfolioDto portfolioDto);

         StockDto removeStock(String portfolioName,String symbol, int quantity);

    double calculatePortfolioValue(String portfolioName, LocalDate date);

    double comparePerformance(
            String yourPortfolioName, String benchmarkSymbol, LocalDate dateFrom, LocalDate dateTo);

    List<PortfolioDto>  getPortfolios(String userName);

    List<PortfolioDto> getAllPortfolios();

    void removePortfolio(String userName, String portfolioName);

    StockDto addStock(String userName, String portfolioName, String symbol, int quantity);

  void addToWatchList(String userName, String symbol)
      throws SymbolNotFoundException;

   void removeFromWatchList(String userName, String symbol);
}
