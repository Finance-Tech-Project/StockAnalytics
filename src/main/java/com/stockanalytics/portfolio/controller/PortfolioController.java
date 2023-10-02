package com.stockanalytics.portfolio.controller;
import com.stockanalytics.dao.SymbolRepository;
import com.stockanalytics.portfolio.dto.StockDto;
import com.stockanalytics.portfolio.service.exeptions.SymbolNotFoundException;
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
    public StockDto addStockToPortfolio(@RequestParam String userName,@RequestParam String portfolioName,@RequestParam String symbol, @RequestParam int quantity) {
      return   portfolioService.addStock(userName,portfolioName,symbol, quantity);
    }
    @PostMapping("/addToWatchList")
    public void addToWatchList(@RequestParam String userName,@RequestParam String symbol) throws InterruptedException {
          portfolioService.addToWatchList(userName,symbol);
    }
    @DeleteMapping("/removeFromWatchList")
    public void removeFromWatchList(@RequestParam String userName,@RequestParam String symbol) {

        portfolioService.removeFromWatchList(userName,symbol);
    }
    @PutMapping("/{username}")
    public PortfolioDto updatePortfolio(@PathVariable String username, @RequestBody PortfolioDto portfolioDto) {
		return portfolioDto;
        // Реализуйте обновление портфолио
    }
    // Удалить акцию из портфеля
    @DeleteMapping("/removeStock")
    public StockDto removeStockFromPortfolio(@RequestParam String portfolioName,@RequestParam String symbol, @RequestParam int quantity) {
      return   portfolioService.removeStock(portfolioName,symbol, quantity);
    }

    @DeleteMapping("/{username}")
    public void deletePortfolio(@PathVariable String username) {
        // Реализуйте удаление портфолио
    }
    // Рассчитать стоимость портфеля на указанную дату
    @GetMapping("/calculateValue")
    public double calculatePortfolioValue(@RequestParam LocalDate date,@PathVariable String portfolioName) {
        return portfolioService.calculatePortfolioValue(date,portfolioName);
    }

    // Проверить существование акций в портфеле
//    @GetMapping("/validateStocksExistence")
//    public boolean validateStocksExistence(@RequestParam String symbol,@RequestParam LocalDate date) {
//        return portfolioService.isTickerValidOnDate(symbol,  date);
//    }

    // Сравнить доходность портфеля с индексом или компанией за указанный период
    @GetMapping("/comparePerformance")
    public double comparePerformance(@RequestParam LocalDate dateFrom, @RequestParam LocalDate dateTo, @RequestParam String benchmarkSymbol) {
        return portfolioService.comparePerformance(dateFrom, dateTo, benchmarkSymbol);
    }

    // Получить список всех портфелей
    @GetMapping("/getAllPortfolios")
    public List<PortfolioDto> getAllPortfolios() {
        return portfolioService.getAllPortfolios();
    }

}

