package org.stockanalytics.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.stockanalytics.model.Symbol;

import java.util.List;

public interface SymbolRepository extends JpaRepository<Symbol, String> {
    List<Symbol> findAllByStatusIsGreaterThan( int status);
}
