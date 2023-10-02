package com.stockanalytics.portfolio.service;
import java.time.LocalDate;
import java.util.List;


import com.stockanalytics.portfolio.dto.PortfolioDto;
import com.stockanalytics.portfolio.dto.StockDto;
import com.stockanalytics.portfolio.model.Portfolio;
import com.stockanalytics.portfolio.service.exeptions.SymbolNotFoundException;

public  interface PortfolioService {
      PortfolioDto createPortfolio(PortfolioDto portfolioDto);

         StockDto removeStock(String portfolioName,String symbol, int quantity);


    double calculatePortfolioValue(LocalDate date, String portfolioName);

    double comparePerformance(LocalDate dateFrom, LocalDate dateTo, String benchmarkSymbol);

    List<PortfolioDto>  getPortfolios(String userName);

    List<PortfolioDto> getAllPortfolios();

    StockDto addStock(String userName,String portfolioName, String symbol, int quantity);

  void addToWatchList(String userName, String symbol)
      throws SymbolNotFoundException, InterruptedException;

   void removeFromWatchList(String userName, String symbol);
}
