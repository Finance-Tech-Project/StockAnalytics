package com.stockanalytics.portfolio.dao;
import com.stockanalytics.accounting.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stockanalytics.portfolio.model.Portfolio;

import java.util.List;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    boolean existsByUserLogin(String userLogin);

    List<Portfolio> findByUserLogin(UserAccount userLogin);
}
