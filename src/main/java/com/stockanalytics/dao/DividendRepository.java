package com.stockanalytics.dao;

import com.stockanalytics.model.Dividend;
import com.stockanalytics.model.Symbol;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DividendRepository extends JpaRepository<Dividend, Symbol> {
}
