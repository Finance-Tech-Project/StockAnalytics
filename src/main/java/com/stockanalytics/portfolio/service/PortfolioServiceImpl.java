package com.stockanalytics.portfolio.service;
import com.stockanalytics.accounting.dao.UserAccountRepository;
import com.stockanalytics.accounting.dto.exceptions.UserNotFoundException;
import com.stockanalytics.accounting.model.UserAccount;
import com.stockanalytics.accounting.service.UserAccountServiceImpl;
import com.stockanalytics.dao.StockQuoteRepository;
import com.stockanalytics.dao.SymbolRepository;
import com.stockanalytics.dto.StockQuoteDto;
import com.stockanalytics.model.StockQuote;
import com.stockanalytics.model.Symbol;
import com.stockanalytics.portfolio.dao.PortfolioRepository;
import com.stockanalytics.portfolio.dto.PortfolioDto;
import com.stockanalytics.portfolio.dto.StockDto;
import com.stockanalytics.portfolio.model.Portfolio;
import com.stockanalytics.portfolio.service.exeptions.PortfolioNotFoundException;
import com.stockanalytics.portfolio.service.exeptions.StocksNotFoundExсeptions;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PortfolioServiceImpl implements PortfolioService  {
final PortfolioRepository portfolioRepository;
    final UserAccountRepository userAccountRepository;
  final StockQuoteRepository stockQuoteRepository;
  final SymbolRepository symbolRepository;
final ModelMapper modelMapper;
private static final Logger logger = LoggerFactory.getLogger(UserAccountServiceImpl.class);

  @Override
  public PortfolioDto createPortfolio(PortfolioDto portfolioDto) {
    UserAccount user =userAccountRepository.findById(portfolioDto.getUserLogin()).orElseThrow(UserNotFoundException::new);
    if (user.getLogin().equals(portfolioDto.getUserLogin())) {
      Map<String, Integer> stockData = portfolioDto.getStocks();
      String userLogin = portfolioDto.getUserLogin();
      UserAccount userAccount = userAccountRepository.getById(userLogin);
      Portfolio portfolio = modelMapper.map(portfolioDto, Portfolio.class);
      portfolio.setStocks(stockData);
      LocalDate portfolioDate = portfolioDto.getPortfolioDate();
      portfolio.setPortfolioDate(portfolioDate);
      portfolio.setUserLogin(userAccount);

            portfolioRepository.save(portfolio);
      return modelMapper.map(portfolio,PortfolioDto.class);
    }
      return  null;
    }



  @Override
  public StockDto addStock(Long portfolioId, String symbol, int quantity) {
    // Находим портфель по его id
    Portfolio portfolio = portfolioRepository.findById(portfolioId)
            .orElseThrow(PortfolioNotFoundException::new);

    // Получаем текущие акции в портфеле (если есть)
    Map<String, Integer> stocks = portfolio.getStocks();

    // Обновляем количество акций для указанного символа
    stocks.put(symbol, stocks.getOrDefault(symbol, 0) + quantity);

    // Обновляем акции в портфеле
    portfolio.setStocks(stocks);

    // Сохраняем обновленный портфель в репозитории
    portfolioRepository.save(portfolio);
    return modelMapper.map(portfolio,StockDto.class);
  }

  public StockDto removeStock(Long portfolioId, String symbol, int quantity) {
    Portfolio portfolio = portfolioRepository.findById(portfolioId)
            .orElseThrow(() -> new PortfolioNotFoundException());

    Map<String, Integer> stocks = portfolio.getStocks();
    if (stocks.containsKey(symbol)) {
      int currentQuantity = stocks.get(symbol);

      // Проверяем, что в портфеле есть достаточно акций для удаления
      if (currentQuantity >= quantity) {
        // Уменьшаем количество акций в портфеле на указанное количество
        stocks.put(symbol, currentQuantity - quantity);
        portfolio.setStocks(stocks);
        portfolioRepository.save(portfolio);
        return modelMapper.map(portfolio, StockDto.class);
      } else {
        logger.error("not enough stocks to delete", portfolioId);
        throw new StocksNotFoundExсeptions();
      }
    } else {
      logger.error("There is no stock with this symbol in the portfolio", portfolioId);
      throw new PortfolioNotFoundException();
    }

  }
  @Override
  public double calculatePortfolioValue(LocalDate date, Long portfolioId) {
    // Получаем портфель по ID
    Portfolio portfolio = portfolioRepository.findById(portfolioId)
            .orElseThrow(() -> new PortfolioNotFoundException());

    // Получаем список акций и их количество из портфеля
    Map<String, Integer> stocks = portfolio.getStocks();

    // Инициализируем общую стоимость портфеля
    double portfolioValue = 0.0;

    // Проходимся по каждой акции в портфеле
    for (Map.Entry<String, Integer> entry : stocks.entrySet()) {
      String symbol = entry.getKey();
      int quantity = entry.getValue();

      // Проверяем актуальность тикера на указанную дату
      if (isTickerValidOnDate(symbol, date)) {
        // Получаем цену акции на указанную дату
        double stockPrice = getStockPriceOnDate(symbol, date);

        // Вычисляем стоимость акции в портфеле
        double stockValue = stockPrice * quantity;

        // Добавляем стоимость акции к общей стоимости портфеля
        portfolioValue += stockValue;
      }
    }

    return portfolioValue;
  }

  private boolean isTickerValidOnDate(String symbol, LocalDate date) {
    List<StockQuote> quotes = stockQuoteRepository.findAllById_Symbol(symbol);
    for (StockQuote quote : quotes) {
      if (quote.getDate().isAfter(date)) {
        return true;
      }
    }
    return false; // Если не найдено подходящих котировок на указанную дату
  }


  private double getStockPriceOnDate(String symbol, LocalDate date) {
    List<StockQuote> quotes = stockQuoteRepository.findAllById_Symbol(symbol);
    if (!quotes.isEmpty()) {
      // Возвращаем цену первой найденной котировки (предполагается, что там будет только одна котировка на дату)
      return quotes.get(0).getClose();
    }
    return  0;
  }




    @Override
    public double comparePerformance(LocalDate dateFrom, LocalDate dateTo, String benchmarkSymbol) {
        return 0;
    }

    @Override
    public List<PortfolioDto>  getPortfolios(String userName) {
      UserAccount userAccount = userAccountRepository.findById(userName).orElseThrow(UserNotFoundException::new);
//      List<Portfolio> portfolios = portfolioRepository.findByUserLogin(userAccount);
        return portfolioRepository.findByUserLogin(userAccount).stream()
                .map(p->modelMapper.map(p,PortfolioDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<PortfolioDto> getAllPortfolios() {

      return portfolioRepository.findAll().stream()
              .map(p -> modelMapper.map(p, PortfolioDto.class))
              .collect(Collectors.toList());
    }
}
