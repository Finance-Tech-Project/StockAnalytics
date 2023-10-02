package com.stockanalytics.portfolio.service;
import com.stockanalytics.accounting.dao.UserAccountRepository;
import com.stockanalytics.accounting.dto.exceptions.UserNotFoundException;
import com.stockanalytics.accounting.model.UserAccount;
import com.stockanalytics.accounting.service.UserAccountServiceImpl;
import com.stockanalytics.dao.StockQuoteRepository;
import com.stockanalytics.dao.SymbolRepository;
import com.stockanalytics.model.StockQuote;
import com.stockanalytics.model.Symbol;
import com.stockanalytics.portfolio.dao.PortfolioRepository;
import com.stockanalytics.portfolio.dto.PortfolioDto;
import com.stockanalytics.portfolio.dto.StockDto;
import com.stockanalytics.portfolio.model.Portfolio;
import com.stockanalytics.portfolio.service.exeptions.PortfolioNotFoundException;
import com.stockanalytics.portfolio.service.exeptions.StocksNotFoundExсeptions;
import com.stockanalytics.portfolio.service.exeptions.SymbolNotFoundException;
import com.stockanalytics.portfolio.service.exeptions.SymbolNotInWatchlistException;
import com.stockanalytics.service.SymbolService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.HashMap;
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
  private final SymbolService symbolService;
private static final Logger logger = LoggerFactory.getLogger(UserAccountServiceImpl.class);
  @Override
  public PortfolioDto createPortfolio(PortfolioDto portfolioDto) {
    UserAccount user = userAccountRepository.findById(portfolioDto.getUserLogin()).orElseThrow(UserNotFoundException::new);
    if (user.getLogin().equals(portfolioDto.getUserLogin())) {
      String userLogin = portfolioDto.getUserLogin();

      LocalDate portfolioDate = portfolioDto.getPortfolioDate();
      String portfolioName = portfolioDto.getPortfolioName(); // Получаем имя портфеля
      // Получаем вотчлист пользователя
      List<String> watchlist = user.getWatchlist();
      // Создаем портфель с акциями из вотчлиста
      Map<String, Integer> stockData = new HashMap<>();
      for (String stockSymbol : watchlist) {
        Symbol symbol = symbolRepository.getByName(stockSymbol);
        // Предполагается, что начальное количество акций в портфеле равно 1
        stockData.put(symbol.getName(), 1);
      }
      Portfolio portfolio = modelMapper.map(portfolioDto, Portfolio.class);
      portfolio.setStocks(stockData);
      portfolio.setPortfolioDate(portfolioDate);
      portfolio.setPortfolioName(portfolioName); // Устанавливаем имя портфеля
      portfolio.setUserLogin(user);
      portfolioRepository.save(portfolio);
      return modelMapper.map(portfolio, PortfolioDto.class);
    }
    return null;
  }

@Override
public void addToWatchList(String userName, String symbol) throws InterruptedException {
  UserAccount user = userAccountRepository.findById(userName).orElseThrow(UserNotFoundException::new);
  Symbol newSymbol;
  try
  {
     newSymbol = symbolRepository.getByName(symbol);
     logger.info("Добавлен новый символ: {}", symbol);
     if (newSymbol==null){
       throw  new SymbolNotFoundException();
     }
  } catch (SymbolNotFoundException exception){
    symbolService.loadSymbolFromYf(symbol);
    newSymbol=symbolService.getSymbol(symbol);
    logger.info("Добавлен новый символ: {}", symbol);
  }
  if (newSymbol==null){

    logger.error("Символ '{}' не найден в базе данных и не удалось загрузить с сайта Yahoo Finance.", symbol);
    throw  new SymbolNotFoundException();
  }
  List<String> watchlist = user.getWatchlist();
  if (!watchlist.contains(symbol)) {
    watchlist.add(symbol);
    user.setWatchlist(watchlist);
    userAccountRepository.save(user);
  }logger.error("Символ '{}' уже есть в watchList.", symbol);

}

  @Override
  public void removeFromWatchList(String userName, String symbol) {
    UserAccount user = userAccountRepository.findById(userName).orElseThrow(UserNotFoundException::new);
    // Получите текущий вотчлист пользователя
    List<String> watchlist = user.getWatchlist();
    // Проверьте, существует ли символ в вотчлисте
    if (watchlist.contains(symbol)) {
      // Если символ найден в вотчлисте, удалите его
      watchlist.remove(symbol);
      // Обновите вотчлист пользователя
      user.setWatchlist(watchlist);
      // Сохраните изменения в базе данных
      userAccountRepository.save(user);
    }
  }


  @Override
  public StockDto addStock(String userName,String portfolioName, String symbol, int quantity) {
    // Находим пользователя по его идентификатору
    UserAccount user = userAccountRepository.findById(userName)
            .orElseThrow(UserNotFoundException::new);
//    if (user.getLogin().equals(portfolioDto.getUserLogin())) {
    // Находим портфель пользователя по имени портфеля
    Portfolio portfolio = portfolioRepository.getByPortfolioName(portfolioName) ;
    if(user.getLogin().equals(userName)&& portfolio!=null) {
      // Получаем текущий вотчлист пользователя
      List<String> watchlist = user.getWatchlist();
      // Проверяем, есть ли указанный символ в вотчлисте
      if (watchlist.contains(symbol)) {
        // Получаем текущие акции в портфеле (если есть)
        Map<String, Integer> stocks = portfolio.getStocks();
        // Обновляем количество акций для указанного символа
        stocks.put(symbol, stocks.getOrDefault(symbol, 0) + quantity);
        // Обновляем акции в портфеле
        portfolio.setStocks(stocks);
        // Сохраняем обновленный портфель в репозитории
        portfolioRepository.save(portfolio);
        // Возвращаем обновленные данные портфеля в виде объекта StockDto
        return modelMapper.map(portfolio, StockDto.class);
      } else {
        // Если символ не найден в вотчлисте, можно выбросить исключение или обработать эту ситуацию соответствующим образом.
        throw new SymbolNotInWatchlistException();
      }
    } else {
      // Если портфель не был найден, можно выбросить исключение или обработать эту ситуацию соответствующим образом.
      throw new PortfolioNotFoundException();
    }
    }


@Override
  public StockDto removeStock(String portfolioName, String symbol, int quantity) {
    Portfolio portfolio = portfolioRepository.getByPortfolioName(portfolioName);
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
        logger.error("not enough stocks to delete", portfolioName);
        throw new StocksNotFoundExсeptions();
      }
    } else {
      logger.error("There is no stock with this symbol in the portfolio", portfolioName);
      throw new PortfolioNotFoundException();
    }
  }
  @Override
  public double calculatePortfolioValue(LocalDate date, String portfolioName) {
    // Получаем портфель по ID
    Portfolio portfolio = portfolioRepository.getByPortfolioName(portfolioName);
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
    List<StockQuote> quotes = stockQuoteRepository.findAllById_Symbol_Name(symbol);
    for (StockQuote quote : quotes) {
      if (quote.getDate().isAfter(date)) {
        return true;
      }
    }
    return false; // Если не найдено подходящих котировок на указанную дату
  }


  private double getStockPriceOnDate(String symbol, LocalDate date) {
    List<StockQuote> quotes = stockQuoteRepository.findAllById_Symbol_Name(symbol);
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

