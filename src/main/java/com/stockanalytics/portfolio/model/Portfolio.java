package com.stockanalytics.portfolio.model;
import java.time.LocalDate;
import java.util.List;
import jakarta.persistence.*;
import com.stockanalytics.accounting.model.UserAccount;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("unused")
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
    private List<PortfolioStocks> stocks;

    public Portfolio(
            UserAccount userLogin,
            String portfolioName,
            LocalDate portfolioDate,
            List<PortfolioStocks> stocks) {
        this.userLogin = userLogin;
        this.portfolioName = portfolioName;
        this.portfolioDate = portfolioDate;
        this.stocks = stocks;
    }
}
