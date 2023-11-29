package com.stockanalytics.portfolio.controller;

import com.stockanalytics.accounting.model.UserAccount;
import com.stockanalytics.dao.SymbolRepository;
import com.stockanalytics.portfolio.dto.PortfolioValueDto;
import com.stockanalytics.portfolio.dto.StockDto;
import com.stockanalytics.portfolio.dto.WatchlistDto;
import com.stockanalytics.portfolio.service.exeptions.SymbolNotFoundException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import com.stockanalytics.portfolio.model.Portfolio;
import com.stockanalytics.portfolio.dto.PortfolioDto;
import com.stockanalytics.portfolio.service.PortfolioService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/portfolio")
public class PortfolioController {
  private final PortfolioService portfolioService;

  public PortfolioController(PortfolioService portfolioService) {
    this.portfolioService = portfolioService;
  }

  // Create a new portfolio
  @PostMapping("/create")
  public PortfolioDto createPortfolio(@RequestBody PortfolioDto portfolioDto) {
    return portfolioService.createPortfolio(portfolioDto);
  }
@GetMapping("/watchList")
public  List<WatchlistDto> getWatchlist(@RequestParam String username) {
  System.out.println("in controller getWatchlist");
  return portfolioService.getWatchlist(username);
}

  @GetMapping("/{username}")
  public List<PortfolioDto> getPortfolios(@PathVariable String username) {
    return portfolioService.getPortfolios(username);
      }
//We receive portfolio prices for each day
  @GetMapping("/getPortfolioValues")
      public List<PortfolioValueDto> getPortfolioValues(
          @RequestParam String portfolioName,
          @RequestParam("fromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
          @RequestParam("toDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
    return portfolioService. getPortfolioValues(portfolioName,fromDate,toDate);
  }
  // Add a stock to the portfolio
  @PostMapping("/addStock")
  public StockDto addStockToPortfolio(
      @RequestParam String userName,
      @RequestParam String portfolioName,
      @RequestParam String symbol,
      @RequestParam int quantity) {
    return portfolioService.addStock(userName, portfolioName, symbol, quantity);
  }

  @PostMapping("/addToWatchList")
  public void addToWatchList(@RequestParam String userName, @RequestParam String symbol)
      throws InterruptedException {
    portfolioService.addToWatchList(userName, symbol);
  }

  @DeleteMapping("/removeFromWatchList")
  public void removeFromWatchList(@RequestParam String userName, @RequestParam String symbol) {
      portfolioService.removeFromWatchList(userName, symbol);
  }

  @PutMapping("/{username}")
  public PortfolioDto updatePortfolio(
      @PathVariable String username, @RequestBody PortfolioDto portfolioDto) {
    return portfolioDto;

  }

  // Remove a stock from the portfolio
  @DeleteMapping("/removeStock")
  public StockDto removeStockFromPortfolio(
      @RequestParam String portfolioName, @RequestParam String symbol, @RequestParam int quantity) {
    return portfolioService.removeStock(portfolioName, symbol, quantity);
  }

  @DeleteMapping("/removePortfolio")
  public void deletePortfolio(@RequestParam String userName,@RequestParam String portfolioName) {
   portfolioService.removePortfolio(userName,portfolioName);
  }

  //Calculate the value of the portfolio as of the specified date
  @GetMapping("/value")
  public double calculatePortfolioValue(@RequestParam String portfolioName, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
    return portfolioService.calculatePortfolioValue(portfolioName, date);
  }
//Portfolio and symbol price comparison
  @GetMapping("/comparePerformance")
  public double comparePerformance(
      @RequestParam String yourPortfolioName, @RequestParam String benchmarkSymbol,
      @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam LocalDate dateFrom,
      @DateTimeFormat(pattern = "yyyy-MM-dd")  @RequestParam LocalDate dateTo) {
    return portfolioService.comparePerformance(yourPortfolioName, benchmarkSymbol, dateFrom, dateTo);
  }

  // Get a list of all portfolios
  @GetMapping("/getAllPortfolios")
  public List<PortfolioDto> getAllPortfolios() {
    return portfolioService.getAllPortfolios();
  }
}
