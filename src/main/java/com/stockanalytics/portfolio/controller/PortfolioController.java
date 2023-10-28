package com.stockanalytics.portfolio.controller;

import com.stockanalytics.dao.SymbolRepository;
import com.stockanalytics.portfolio.dto.StockDto;
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

  // Создать новый портфель
  @PostMapping("/create")
  public PortfolioDto createPortfolio(@RequestBody PortfolioDto portfolioDto) {
    return portfolioService.createPortfolio(portfolioDto);
  }

  @GetMapping("/{username}")
  public List<PortfolioDto> getPortfolios(@PathVariable String username) {
    return portfolioService.getPortfolios(username);
    // Реализуйте получение портфолио по имени пользователя
  }

  // Добавить акцию в портфель
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
    // Реализуйте обновление портфолио
  }

  // Удалить акцию из портфеля
  @DeleteMapping("/removeStock")
  public StockDto removeStockFromPortfolio(
      @RequestParam String portfolioName, @RequestParam String symbol, @RequestParam int quantity) {
    return portfolioService.removeStock(portfolioName, symbol, quantity);
  }

  @DeleteMapping("/removePortfolio")
  public void deletePortfolio(@RequestParam String userName,@RequestParam String portfolioName) {
   portfolioService.removePortfolio(userName,portfolioName);
  }

  // Рассчитать стоимость портфеля на указанную дату
  @GetMapping("/value")
  public double calculatePortfolioValue(@RequestParam String portfolioName, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
    return portfolioService.calculatePortfolioValue(portfolioName, date);
  }

  // Проверить существование акций в портфеле
  //    @GetMapping("/validateStocksExistence")
  //    public boolean validateStocksExistence(@RequestParam String symbol,@RequestParam LocalDate
  // date) {
  //        return portfolioService.isTickerValidOnDate(symbol,  date);
  //    }

  // Сравнить доходность портфеля с индексом или компанией за указанный период
  @GetMapping("/comparePerformance")
  public double comparePerformance(
      @RequestParam String yourPortfolioName, @RequestParam String benchmarkSymbol,
      @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam LocalDate dateFrom,
      @DateTimeFormat(pattern = "yyyy-MM-dd")  @RequestParam LocalDate dateTo) {
    return portfolioService.comparePerformance(yourPortfolioName, benchmarkSymbol, dateFrom, dateTo);
  }

  // Получить список всех портфелей
  @GetMapping("/getAllPortfolios")
  public List<PortfolioDto> getAllPortfolios() {
    return portfolioService.getAllPortfolios();
  }
}
