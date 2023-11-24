package com.stockanalytics.dao;

import com.stockanalytics.model.Dividend;
import com.stockanalytics.model.Symbol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface DividendRepository extends JpaRepository<Dividend, Symbol> {
    @Query("select d from Dividend d where d.symbol = :symbol and d.date between :startdate and :enddate")
    List<Dividend> findAllBySymbolAndDateBetween(@Param("symbol") Symbol symbol,
                                               @Param("startdate") LocalDate startDate,
                                               @Param("enddate") LocalDate endDate);




}
