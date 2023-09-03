package org.stockanalytics.util;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.stockanalytics.dto.StockQuoteDto;

import java.io.StringReader;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

public class HistoryDateGetter {
    private final RestTemplate restTemplate = new RestTemplate();



    public List<StockQuoteDto> getAllHistoryStockQuotes(String symbol) {
        LocalDate startDate = LocalDate.of(2001,1,1);
        long startTimestamp = startDate.atStartOfDay().toInstant(ZoneOffset.UTC).getEpochSecond();
        LocalDate endDate = LocalDate.now().minusDays(1);
        long endTimestamp = endDate.atStartOfDay().toInstant(ZoneOffset.UTC).getEpochSecond();

        String BASE_URL = "https://query1.finance.yahoo.com/v7/finance/download/%s";
        String urlString = String.format(BASE_URL, symbol) +
                "?period1=" + startTimestamp +
                "&period2=" + endTimestamp +
                "&interval=" + "1d" +
                "&events=" +  "history" +
                "&includeAdjustedClose=" + "true";

        System.out.println(urlString);
        ResponseEntity<String> response = restTemplate.exchange(
                urlString,
                HttpMethod.GET,
                null,
                String.class
        );

        String csvData = response.getBody();
        CSVReader csvReader = null;
        if (csvData != null) {
            csvReader = new CSVReader(new StringReader(csvData));
        }
        CsvToBean<StockQuoteDto> csvToBean = null;
        if (csvReader != null) {
            csvToBean = new CsvToBeanBuilder<StockQuoteDto>(csvReader)
                    .withType(StockQuoteDto.class)
                    .withSeparator(',')
                    .build();
        }

        List<StockQuoteDto> stockQuoteDtos = null;
        if (csvToBean != null) {
            stockQuoteDtos = csvToBean.parse();
        }
//        System.out.println(stockQuoteDtos);
        return stockQuoteDtos;
    }
}
