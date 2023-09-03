package org.stockanalytics.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.stockanalytics.model.Symbol;

public interface SymbolRepository extends JpaRepository<Symbol, String> {

}
