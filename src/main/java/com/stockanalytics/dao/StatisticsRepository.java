package com.stockanalytics.dao;

import com.stockanalytics.model.Symbol;
import com.stockanalytics.model.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatisticsRepository extends JpaRepository<Statistics, Symbol> {

}
