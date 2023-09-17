package com.stockanalytics.dao;

import com.stockanalytics.model.Statistics;
import com.stockanalytics.model.Symbol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StatisticsRepository extends JpaRepository<Statistics, Symbol> {

 @Query("select s from Statistics s where s.symbol = :symbol ")
 Optional<Statistics> findBySymbol(@Param("symbol") Symbol symbol);

 @Query("select case when count (s) > 0 then true else false end from Statistics s where s.symbol = :symbol")
 boolean existsBySymbol(Symbol symbol);

}
