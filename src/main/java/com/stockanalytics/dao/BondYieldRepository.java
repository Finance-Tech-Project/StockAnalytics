package com.stockanalytics.dao;

import com.stockanalytics.model.BondYield;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BondYieldRepository  extends JpaRepository<BondYield, Double> {

    @Query("SELECT b FROM BondYield b WHERE b.date BETWEEN :dateFrom AND :dateTo")
    List<BondYield> findBondYieldsBetweenDates(@Param("dateFrom") LocalDate dateFrom, @Param("dateTo") LocalDate dateTo);
}
