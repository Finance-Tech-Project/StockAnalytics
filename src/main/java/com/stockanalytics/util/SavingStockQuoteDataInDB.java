package com.stockanalytics.util;

import com.stockanalytics.dto.StockQuoteDto;
import com.stockanalytics.model.Symbol;
import lombok.RequiredArgsConstructor;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Transactional
public class SavingStockQuoteDataInDB {

    @Value("${spring.datasource.url}")
    private final String datasourceUrl;

    @Value("${spring.datasource.username}")
    private final String datasourceUsername;

    @Value("${spring.datasource.password}")
    private final String datasourcePassword;

    private final QuoteDataRounding quoteDataRounding;

    public void saveStockQuoteData(Symbol symbol, List<StockQuoteDto> quotes) {
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
    }

    private String formatToCsvLine(StockQuoteDto quote, Symbol symbol) {
        // Assuming CSV format: date, symbol, open, high, low, close, volume
        return String.format("%s,%s,%s,%s,%s,%s,%d",
                quote.getDate(),
                symbol.getName(),
                quoteDataRounding.round(quote.getOpen()),
                quoteDataRounding.round(quote.getHigh()),
                quoteDataRounding.round(quote.getLow()),
                quoteDataRounding.round(quote.getClose()),
                quote.getVolume());
    }

    @SuppressWarnings("SameParameterValue")
    private void copyData(String data, String tableName) throws SQLException {
        String copySql = "COPY " + tableName +
                " (date, symbol_name, open, high, low, close, volume) FROM STDIN WITH (FORMAT csv)";
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
}
