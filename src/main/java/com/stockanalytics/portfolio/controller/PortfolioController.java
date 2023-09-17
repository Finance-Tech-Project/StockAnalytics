//package com.stockanalytics.portfolio.controller;
//import org.springframework.web.bind.annotation.*;
//
//import com.stockanalytics.portfolio.dto.PortfolioDto;
//import com.stockanalytics.portfolio.service.PortfolioService;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/portfolio")
//public class PortfolioController {
//    private final PortfolioService portfolioService;
//
//    public PortfolioController(PortfolioService portfolioService) {
//        this.portfolioService = portfolioService;
//    }
//
//    @PostMapping
//    public PortfolioDto createPortfolio(@RequestBody PortfolioDto portfolioDto) {
//		return portfolioDto;
//        // Реализуйте создание портфолио
//    }
//
//    @GetMapping("/{username}")
//    public PortfolioDto getPortfolio(@PathVariable String username) {
//		return null;
//        // Реализуйте получение портфолио по имени пользователя
//    }
//
//    @PutMapping("/{username}")
//    public PortfolioDto updatePortfolio(@PathVariable String username, @RequestBody PortfolioDto portfolioDto) {
//		return portfolioDto;
//        // Реализуйте обновление портфолио
//    }
//
//    @DeleteMapping("/{username}")
//    public void deletePortfolio(@PathVariable String username) {
//        // Реализуйте удаление портфолио
//    }
//
//    // Дополнительные методы, если необходимо
//}
//
