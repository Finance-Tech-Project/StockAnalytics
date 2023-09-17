package com.stockanalytics.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.stockanalytics.model.Symbol;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SymbolRepository extends JpaRepository<Symbol, String> {
    List<Symbol> findAllByStatusIsGreaterThan( int status);

    @Query("select s from Symbol s where s.isStarting=1 ")
    List<Symbol> findAllByIsStartingEquals(int isStarting);

    Symbol getByName(String ticker);
}
