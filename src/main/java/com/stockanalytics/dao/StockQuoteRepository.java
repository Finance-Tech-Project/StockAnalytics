package com.stockanalytics.dao;

import com.stockanalytics.model.StockQuote;
import com.stockanalytics.model.Symbol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface StockQuoteRepository extends JpaRepository<StockQuote, LocalDate> {
    @Query("select r from StockQuote r where r.id.symbol = :symbol and r.id.date = :date")
    StockQuote getBySymbolAndDate(@Param("symbol") Symbol symbol,
                                 @Param("date") LocalDate date);

    @Query("select r from StockQuote r where r.id.symbol = :symbol and r.id.date between :startdate and :enddate")
    List<StockQuote> findAllByIdIdAndDateBetween(@Param("symbol") Symbol symbol,
                                                    @Param("startdate") LocalDate startDate,
                                                    @Param("enddate") LocalDate endDate);

    @Query("select distinct r.id.symbol from StockQuote r")
    List<String> getAllSymbols();

    @Query("select r.id.date from StockQuote r where r.id.symbol = :index order by r.id.date")
    List<LocalDate> findAllDatedBySymbol(@Param("index") String s);

    List<StockQuote> findAllById_Symbol(Symbol symbol);

    boolean existsById_Symbol(Symbol symbol);

    @Query(value = "select sq.id.date from StockQuote sq where sq.id.symbol = :symbol order by sq.id.date desc")
    List<LocalDate>   getQuotDatesList(@Param("symbol") Symbol symbol);

    List<StockQuote> findAllById_Symbol_Name(String symbol);
    @Query("SELECT sq.close FROM StockQuote sq WHERE sq.id.symbol.name = :symbol AND sq.id.date = :date")
    Double findStockPriceBySymbolNameAndDate(@Param("symbol") String symbolName, @Param("date") LocalDate date);

}
