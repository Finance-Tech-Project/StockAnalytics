package com.stockanalytics.service;

import com.stockanalytics.dao.StockQuoteRepository;
import com.stockanalytics.dao.SymbolRepository;
import com.stockanalytics.dto.StatisticsDto;
import com.stockanalytics.dto.StockQuoteDto;
import com.stockanalytics.model.StockQuote;
import com.stockanalytics.model.StockQuoteId;
import com.stockanalytics.model.Symbol;
import com.stockanalytics.util.DataGetter;
import com.stockanalytics.util.StockQuoteProcessor;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Repository
public class StockQuoteService {

    final StockQuoteRepository stockQuoteRepository;
    final SymbolRepository symbolRepository;
    final StockQuoteProcessor processor = new StockQuoteProcessor();
    final DataGetter getter = new DataGetter();
    final DecimalFormat df = new DecimalFormat("#.##");

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Value("${spring.datasource.username}")
    private String datasourceUsername;

    @Value("${spring.datasource.password}")
    private String datasourcePassword;

    private double round(double number) {
        String str = df.format(number).replace(",", ".");
        return Double.parseDouble(str);
    }

    @Transactional
//    private void loadAll(Symbol symbol){
//        List<StockQuoteDto> quotes = getter.getAllHistoryStockQuotes(symbol);
//        for (StockQuoteDto quote : quotes) {
//            StockQuoteId id = new StockQuoteId(quote.getDate(), symbol);
//            StockQuote stockQuote = new StockQuote(id, round(quote.getOpen()), round(quote.getHigh()), round(quote.getLow()), round(quote.getClose()), quote.getVolume());
//            stockQuoteRepository.save(stockQuote);
//        }
//    }

    private void loadAll(Symbol symbol) {
        List<StockQuoteDto> quotes = getter.getAllHistoryStockQuotes(symbol);
        String csvData = quotes.stream()
                .map(quote -> formatToCsvLine(quote, symbol))
                .collect(Collectors.joining("\n"));

        if (!csvData.isEmpty()) {
            try {
                System.out.println(new Timestamp(System.currentTimeMillis()) + ": Start copying data for " + symbol.getName());
                copyData(csvData, "stock_quote"); // Assuming 'stock_quotes' is your table name
                System.out.println(new Timestamp(System.currentTimeMillis()) + ": End copying data for " + symbol.getName());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        symbol.setStatus(1);
        symbolRepository.save(symbol);
    }

    private String formatToCsvLine(StockQuoteDto quote, Symbol symbol) {
        StockQuoteId id = new StockQuoteId(quote.getDate(), symbol);
        StockQuote stockQuote = new StockQuote(id, round(quote.getOpen()), round(quote.getHigh()), round(quote.getLow()), round(quote.getClose()), quote.getVolume());
        // Assuming CSV format: date, symbol, open, high, low, close, volume
        return String.format("%s,%s,%f,%f,%f,%f,%d",
                quote.getDate(),
                symbol.getName(),
                round(quote.getOpen()),
                round(quote.getHigh()),
                round(quote.getLow()),
                round(quote.getClose()),
                quote.getVolume());
    }

    private void copyData(String data, String tableName) throws SQLException {
        String copySql = "COPY " + tableName + " (date, symbol_name, open, high, low, close, volume) FROM STDIN WITH (FORMAT csv)";
        try (Connection conn = DriverManager.getConnection(datasourceUrl, datasourceUsername, datasourcePassword)) {
            CopyManager copyManager = new CopyManager(conn.unwrap(BaseConnection.class));
            try (ByteArrayInputStream input = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8))) {
                copyManager.copyIn(copySql, input);
            }
        } catch (Exception e) {
            // Handle exception properly: log or throw as needed
            System.err.println("Error during COPY to PostgreSQL: " + e.getMessage());
            throw new RuntimeException("Failed to COPY data", e);
        }
    }

    @Transactional
    public List<StockQuoteDto> getData(Symbol symbol, LocalDate dateFrom, LocalDate dateTo) throws SQLException {
        if (symbol.getStatus() == 0) {
            loadAll(symbol);
        }
        return getQuotesByPeriod(dateFrom, dateTo, symbol);
    }

    public StockQuote getSingleDate(Symbol symbol, LocalDate date) throws SQLException {
        if (symbol.getStatus() == 0) {
            loadAll(symbol);
        }
        return stockQuoteRepository.getBySymbolAndDate(symbol, date);
    }

    public List<StockQuote> getListByIdAndDateBetween(Symbol symbol, LocalDate dateFrom, LocalDate dateTo) throws SQLException {
        if (symbol.getStatus() == 0) {
            loadAll(symbol);
        }
        return stockQuoteRepository.findAllByIdIdAndDateBetween(symbol, dateFrom, dateTo);
    }

    public List<List<StockQuoteDto>> getListsForChart(Symbol symbol, LocalDate dateFrom, LocalDate dateTo) throws SQLException {
        List<StockQuoteDto> list = getData(symbol, dateFrom, dateTo);
        return processor.getAllQuoteLists(list, dateFrom, dateTo);
    }


    public List<StockQuoteDto> getQuotesByPeriod(LocalDate dateFrom, LocalDate dateTo, Symbol symbol) {
        List<StockQuote> quotes = stockQuoteRepository.findAllById_Symbol(symbol);
        LocalDate start = dateFrom.withMonth(1).withDayOfMonth(1);
        LocalDate end = dateTo.withMonth(12).withDayOfMonth(31);
        return quotes.stream()
                .filter(quote -> quote.getId().getDate().isAfter(start.minusDays(1)) && quote.getId().getDate().isBefore(end.plusDays(1)))
                .map(quote -> new StockQuoteDto(quote.getId().getDate(), quote.getOpen(), quote.getHigh(), quote.getLow(), quote.getClose(), quote.getVolume()))
                .collect(Collectors.toList());
    }

    public List<StatisticsDto> getStatistics(Symbol symbol) {
        Map<String, String> parameters = getter.getDataForStatisticsFromYahoo(symbol);
        return null;
    }
}
