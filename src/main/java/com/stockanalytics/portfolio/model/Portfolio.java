package com.stockanalytics.portfolio.model;

import java.time.LocalDate;
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
    public UserAccount  userLogin;

    private LocalDate portfolioDate;

    @ElementCollection
    @CollectionTable(name = "portfolio_stocks")
    @MapKeyColumn(name = "stock_symbol")
    @Column(name = "stock_quantity")
    private Map<String, Integer> stocks;

    public Portfolio(UserAccount userLogin, LocalDate portfolioDate, Map<String, Integer> stocks) {
        this.userLogin = userLogin;
        this.portfolioDate = portfolioDate;
        this.stocks = stocks;
    }
}
