package com.stockanalytics.portfolio.dao;
import com.stockanalytics.accounting.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stockanalytics.portfolio.model.Portfolio;

import java.util.List;
import java.util.Optional;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    Boolean existsByPortfolioName(String portfolioName);

    List<Portfolio> findByUserLogin(UserAccount userLogin);

    Optional<Object> findByUserLoginAndPortfolioName(UserAccount user, String portfolioName);

    Portfolio getByPortfolioName(String portfolioName);

    void deleteAllByUserLogin(UserAccount userAccount);
}
