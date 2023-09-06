package com.stockanalytics.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.stockanalytics.model.Symbol;

import java.util.List;

public interface SymbolRepository extends JpaRepository<Symbol, String> {
    List<Symbol> findAllByStatusIsGreaterThan( int status);
}
