package com.stockanalytics.portfolio.model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.*;

import com.stockanalytics.accounting.model.UserAccount;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Portfolio {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_login")
  public UserAccount userLogin;

  private LocalDate portfolioDate;
  public String portfolioName;

  @ElementCollection
  @CollectionTable(name = "portfolio_stocks")
  @MapKeyColumn(name = "stock_symbol")
  @Column(name = "stock_quantity")
  private Map<String, Integer> stocks;

  public Portfolio(
      UserAccount userLogin,
      String portfolioName,
      LocalDate portfolioDate,
      Map<String, Integer> stocks) {
    this.userLogin = userLogin;
    this.portfolioName = portfolioName;
    this.portfolioDate = portfolioDate;
    this.stocks = stocks;
  }

  // Method for adding stocks from watchlist
  public void addStocksFromWatchlist(List<String> watchlist) {
    if (this.stocks == null) {
      this.stocks = new HashMap<>();
    }

    // all stocks from the watchlist have an initial quantity of 1
    for (String stock : watchlist) {
      this.stocks.put(stock, 1);
    }
  }
}
