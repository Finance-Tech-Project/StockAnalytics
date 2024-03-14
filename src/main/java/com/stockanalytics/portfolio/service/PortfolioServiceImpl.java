package com.stockanalytics.portfolio.service;

import com.stockanalytics.accounting.dao.UserAccountRepository;
import com.stockanalytics.accounting.dto.exceptions.UserNotFoundException;
import com.stockanalytics.accounting.model.UserAccount;
import com.stockanalytics.accounting.service.UserAccountServiceImpl;
import com.stockanalytics.dao.StockQuoteRepository;
import com.stockanalytics.dao.SymbolRepository;
import com.stockanalytics.dto.SymbolDto;
import com.stockanalytics.model.StockQuote;
import com.stockanalytics.model.Symbol;
import com.stockanalytics.portfolio.dao.PortfolioRepository;
import com.stockanalytics.portfolio.dto.PortfolioDto;
import com.stockanalytics.portfolio.dto.PortfolioValueDto;
import com.stockanalytics.portfolio.dto.StockDto;
import com.stockanalytics.portfolio.dto.WatchlistDto;
import com.stockanalytics.portfolio.model.Portfolio;
import com.stockanalytics.portfolio.service.exceptions.PortfolioNotFoundException;
import com.stockanalytics.portfolio.service.exceptions.StocksNotFoundException;

import com.stockanalytics.portfolio.service.exceptions.SymbolExistInWatchListException;
import com.stockanalytics.portfolio.service.exceptions.SymbolNotFoundException;
import com.stockanalytics.service.SymbolService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("ALL")
@Service
@RequiredArgsConstructor
public class PortfolioServiceImpl implements PortfolioService {
    final PortfolioRepository portfolioRepository;
    final UserAccountRepository userAccountRepository;
    final StockQuoteRepository stockQuoteRepository;
    final SymbolRepository symbolRepository;
    final ModelMapper modelMapper;
    private final SymbolService symbolService;

    static final Logger logger = LoggerFactory.getLogger(UserAccountServiceImpl.class);

    @Override
    public PortfolioDto createPortfolio(PortfolioDto portfolioDto) {
        UserAccount user =
                userAccountRepository
                        .findById(portfolioDto.getUserLogin())
                        .orElseThrow(UserNotFoundException::new);
        if (user.getLogin().equals(portfolioDto.getUserLogin())) {
            LocalDate portfolioDate = portfolioDto.getPortfolioDate();
            String portfolioName = portfolioDto.getPortfolioName();
            List<String> watchlist = user.getWatchlist();
            Map<String, Integer> stockData = new HashMap<>();
            for (String stockSymbol : watchlist) {
                Symbol symbol = symbolRepository.getByName(stockSymbol);
                stockData.put(symbol.getName(), 1);
            }
            Portfolio portfolio = modelMapper.map(portfolioDto, Portfolio.class);
            portfolio.setStocks(stockData);
            portfolio.setPortfolioDate(portfolioDate);
            portfolio.setPortfolioName(portfolioName);
            portfolio.setUserLogin(user);
            portfolioRepository.save(portfolio);
            return modelMapper.map(portfolio, PortfolioDto.class);
        }
        return null;
    }

    public List<WatchlistDto> getWatchlist(String username) {

        UserAccount user = userAccountRepository.findByLogin(username).orElseThrow(UserNotFoundException::new);
        List<String> watchlist = user.getWatchlist();

        // Getting information about symbols from the symbol repository
        return watchlist.stream()
                .map(this::getWatchlistInfo)
                .collect(Collectors.toList());
    }

    @Override
    public WatchlistDto getSymbolInfo(String symbolName) {
        Symbol symbol = symbolRepository.getByName(symbolName);

        if (symbol != null) {
            // Convert the Symbol object to SymbolDto to get the fields you need
            return modelMapper.map(symbol, WatchlistDto.class);
        } else {
            throw new SymbolNotFoundException();
        }
    }

    private WatchlistDto getWatchlistInfo(String symbolName) {
        Symbol symbol = symbolRepository.getByName(symbolName);

        // Gets the last existing date for which there is historical data for the symbol.
        List<LocalDate> dates = stockQuoteRepository.getQuotDatesList(symbol);

        if (symbol != null || !dates.isEmpty()) {
            // Convert the Symbol object to WatchlistDto to get the fields you need
            return WatchlistDto.builder()
                    .symbolName(symbol.getName())
                    .companyName(symbol.getCompanyName())
                    .exchange(symbol.getExchange())
                    .industryCategory(symbol.getIndustryCategory())
                    .hasDividends(symbol.getHasDividends())
                    .close(getStockPriceOnDateOrClosestNext(symbolName, dates.get(0)))
                    .build();
        } else {
            throw new SymbolNotFoundException();
        }
    }

    @Override
    public void addToWatchList(String userName, String symbol) {
        UserAccount user = userAccountRepository.findById(userName).orElseThrow(UserNotFoundException::new);
        Symbol newSymbol = symbolRepository.getByName(symbol);
        if (newSymbol == null || !stockQuoteRepository.existsById_Symbol(newSymbol)) {
            logger.info("Symbol not found in repository: {}", symbol);
            throw new SymbolNotFoundException();
        }
        List<String> watchlist = user.getWatchlist();
        if (!watchlist.contains(symbol)) {
            watchlist.add(symbol);
            user.setWatchlist(watchlist);
            userAccountRepository.save(user);
            logger.info("Symbol successfully added to the watchlist: {}", symbol);
        } else {
            logger.error("symbol '{}' already exists in the watchlist.", symbol);
            throw  new SymbolExistInWatchListException(symbol);
        }
    }

    @Override
    public void removeSymbolsFromWatchList(String userName, List<String> symbols) {
        UserAccount user =
                userAccountRepository.findById(userName).orElseThrow(UserNotFoundException::new);
        if (symbols.isEmpty()) return;
        List<String> watchlist = user.getWatchlist();
        symbols.stream().forEach((symbol) -> {
            if (watchlist.contains(symbol)) {
                watchlist.remove(symbol);
            }
        });
        user.setWatchlist(watchlist);
        userAccountRepository.save(user);
    }

    @Override
    public void removePortfolio(String userName, String portfolioName) {
        UserAccount user =
                userAccountRepository.findById(userName).orElseThrow(UserNotFoundException::new);
        Portfolio portfolio = portfolioRepository.getByPortfolioName(portfolioName);
        if (user.getLogin().equals(userName)
                && portfolio != null
                && portfolio.getPortfolioName().equals(portfolioName)) {
            portfolioRepository.delete(portfolio);
        }
    }

    @Override
    public StockDto addStock(String userName, String portfolioName, String symbol, int quantity) {
        UserAccount user =
                userAccountRepository.findById(userName).orElseThrow(UserNotFoundException::new);
        Portfolio portfolio = portfolioRepository.getByPortfolioName(portfolioName);
        if (user.getLogin().equals(userName) && portfolio != null) {

            List<String> watchlist = user.getWatchlist();
            if (watchlist.contains(symbol)) {
                Map<String, Integer> stocks = portfolio.getStocks();
                stocks.put(symbol, stocks.getOrDefault(symbol, 0) + quantity);
                portfolio.setStocks(stocks);
                portfolioRepository.save(portfolio);
                return modelMapper.map(portfolio, StockDto.class);
            } else {
                throw new SymbolNotFoundException();
            }
        } else {
            throw new PortfolioNotFoundException();
        }
    }

    @Override
    public StockDto removeStock(String portfolioName, String symbol, int quantity) {
        Portfolio portfolio = portfolioRepository.getByPortfolioName(portfolioName);
        Map<String, Integer> stocks = portfolio.getStocks();
        if (stocks.containsKey(symbol) && stocks.get(symbol) == 0) {
            stocks.remove(symbol);
            portfolio.setStocks(stocks);
            portfolioRepository.save(portfolio);
            return modelMapper.map(portfolio, StockDto.class);
        }

        if (stocks.containsKey(symbol)) {
            int currentQuantity = stocks.get(symbol);
            if (currentQuantity >= quantity) {
                stocks.put(symbol, currentQuantity - quantity);
                portfolio.setStocks(stocks);
                portfolioRepository.save(portfolio);
                return modelMapper.map(portfolio, StockDto.class);
            } else {
                logger.error("not enough stocks to delete", portfolioName);
                throw new StocksNotFoundException();
            }
        } else {
            logger.error("There is no stock with this symbol in the portfolio", portfolioName);
            throw new PortfolioNotFoundException();
        }
    }

    public List<PortfolioValueDto> getPortfolioValues(String portfolioName, LocalDate fromDate, LocalDate toDate) {
        List<PortfolioValueDto> portfolioValues = new ArrayList<>();
        for (LocalDate currentDate = fromDate; currentDate.isBefore(toDate.plusDays(1)); currentDate = currentDate.plusDays(1)) {
            double portfolioValue = calculatePortfolioValue(portfolioName, currentDate);
            portfolioValues.add(new PortfolioValueDto(currentDate, portfolioValue));
        }
        return portfolioValues;
    }

    @Override
    public double calculatePortfolioValue(String portfolioName, LocalDate date) {
        Portfolio portfolio = portfolioRepository.getByPortfolioName(portfolioName);
        Map<String, Integer> stocks = portfolio.getStocks();

        return stocks.entrySet().stream()
                .mapToDouble(
                        entry -> {
                            String symbol = entry.getKey();
                            int quantity = entry.getValue();

                            try {
                                double stockPrice = getStockPriceOnDateOrClosestNext(symbol, date);
                                return stockPrice * quantity;
                            } catch (PortfolioNotFoundException e) {

                                logger.error("Error processing a symbol: {}", symbol);
                                return 0.0;
                            }
                        })
                .sum();
    }

    private boolean isTickerValidOnDate(String symbol, LocalDate date) {
        List<StockQuote> quotes = stockQuoteRepository.findAllById_Symbol_Name(symbol);
        for (StockQuote quote : quotes) {
            if (quote.getDate().isAfter(date)) {
                return true;
            }
        }
        return false;
    }

    private double getStockPriceOnDateOrClosestNext(String symbol, LocalDate date) {
        for (int i = 0; i <= 3; i++) {
            LocalDate currentDate = date.minusDays(i);
            Double result = stockQuoteRepository.findStockPriceBySymbolNameAndDate(symbol, currentDate);
            if (result != null) {
                return result;
            }
        }
        throw new PortfolioNotFoundException();
    }

    @Override
    public double comparePerformance(String yourPortfolioName, String benchmarkSymbol, LocalDate dateFrom, LocalDate dateTo) {
        Portfolio yourPortfolio = portfolioRepository.getByPortfolioName(yourPortfolioName);
        if (yourPortfolio == null) {
            throw new PortfolioNotFoundException(); //Checking that your portfolio exists
        }

        // Getting all available symbols from the symbol repository
        List<SymbolDto> benchmarkSymbols = symbolService.getAllSymbols();

        //calculate the value of your portfolio for given dates
        double yourPortfolioValueFrom = calculatePortfolioValue(yourPortfolioName, dateFrom);
        double yourPortfolioValueTo = calculatePortfolioValue(yourPortfolioName, dateTo);
        double benchmarkValueFrom = getStockPriceOnDateOrClosestNext(benchmarkSymbol, dateFrom);
        double benchmarkValueTo = getStockPriceOnDateOrClosestNext(benchmarkSymbol, dateTo);
        //  calculate the performance of your portfolio and the selected index (or portfolio)
        double yourPerformance =
                (yourPortfolioValueTo - yourPortfolioValueFrom) / yourPortfolioValueFrom;
        double benchmarkPerformance = (benchmarkValueTo - benchmarkValueFrom) / benchmarkValueFrom;
        return yourPerformance - benchmarkPerformance;
    }

    @Override
    public List<PortfolioDto> getPortfolios(String userName) {
        UserAccount userAccount = userAccountRepository.findById(userName).orElseThrow(UserNotFoundException::new);
        return portfolioRepository.findByUserLogin(userAccount).stream()
                .map(p -> modelMapper.map(p, PortfolioDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<PortfolioDto> getAllPortfolios() {
        return portfolioRepository.findAll().stream()
                .map(p -> modelMapper.map(p, PortfolioDto.class))
                .collect(Collectors.toList());
    }
}



