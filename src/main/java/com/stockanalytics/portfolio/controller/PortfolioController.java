package com.stockanalytics.portfolio.controller;
import com.stockanalytics.portfolio.dto.StockDto;
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
    public StockDto addStockToPortfolio(@RequestParam Long portfolioId,@RequestParam String symbol, @RequestParam int quantity) {
      return   portfolioService.addStock(portfolioId,symbol, quantity);
    }
    @PutMapping("/{username}")
    public PortfolioDto updatePortfolio(@PathVariable String username, @RequestBody PortfolioDto portfolioDto) {
		return portfolioDto;
        // Реализуйте обновление портфолио
    }
    // Удалить акцию из портфеля
    @DeleteMapping("/removeStock")
    public StockDto removeStockFromPortfolio(@RequestParam Long portfolioId,@RequestParam String symbol, @RequestParam int quantity) {
      return   portfolioService.removeStock(portfolioId,symbol, quantity);
    }

    @DeleteMapping("/{username}")
    public void deletePortfolio(@PathVariable String username) {
        // Реализуйте удаление портфолио
    }
    // Рассчитать стоимость портфеля на указанную дату
    @GetMapping("/calculateValue")
    public double calculatePortfolioValue(@RequestParam LocalDate date,@PathVariable Long portfolioId) {
        return portfolioService.calculatePortfolioValue(date,portfolioId);
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

