package com.stockanalytics.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.stockanalytics.dto.StatisticsDto;
import com.stockanalytics.dto.StockQuoteDto;
import com.stockanalytics.model.Symbol;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.StringReader;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
public class DataGetter {
    private final RestTemplate restTemplate = new RestTemplate();
    ObjectMapper objectMapper = new ObjectMapper();


    public List<StockQuoteDto> getHistoryStockQuotes(LocalDate startDate, LocalDate endDate, Symbol symbol) {
        ZoneId newYorkZone = ZoneId.of("America/New_York");
        ZonedDateTime currentTimeInNewYork = ZonedDateTime.now(newYorkZone);
        ZoneOffset offset = currentTimeInNewYork.getOffset();
        long startTimestamp = startDate.atStartOfDay().toInstant(offset).getEpochSecond();
        long endTimestamp = endDate.atStartOfDay().toInstant(offset).getEpochSecond();
        String ticker = symbol.getName();
        String BASE_URL = "https://query1.finance.yahoo.com/v7/finance/download/%s";
        String urlString = String.format(BASE_URL, ticker) +
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
        symbol.setStatus(1);
        CSVReader csvReader = null;
        if (csvData != null) {
            csvReader = new CSVReader(new StringReader(csvData));
        }
        CsvToBean<StockQuoteDto> csvToBean = new CsvToBeanBuilder<StockQuoteDto>(csvReader)
                .withType(StockQuoteDto.class)
                .withSeparator(',')
                .build();

        return csvToBean.parse();
    }

    public List<StockQuoteDto> getAllHistoryStockQuotes(Symbol symbol){
        return getHistoryStockQuotes(LocalDate.of(2001,1,1), LocalDate.now().minusDays(1), symbol);
    }

    public StatisticsDto getStatistcs(String ticker){
        String BASE_URL = "https://query1.finance.yahoo.com/v6/finance/quoteSummary/%s?modules=defaultKeyStatistics";
        String urlString = String.format(BASE_URL, ticker);


        System.out.println(urlString);
        ResponseEntity<String> response = restTemplate.exchange(
                urlString,
                HttpMethod.GET,
                null,
                String.class
        );

        String csvData = response.getBody();
        System.out.println(csvData);

        return null;
    }

    public Map <String, String> getDataForStatistics(Symbol symbol) throws JsonProcessingException {
        String ticker = symbol.getName();
        String BASE_URL = "https://query1.finance.yahoo.com/v6/finance/quoteSummary/%s?modules=defaultKeyStatistics";
        String urlString = String.format(BASE_URL, ticker);


        System.out.println(urlString);
        ResponseEntity<String> response = restTemplate.exchange(
                urlString,
                HttpMethod.GET,
                null,
                String.class
        );

        String csvData = response.getBody();

        LinkedHashMap<String, Object> json = objectMapper.readValue(response.getBody(), LinkedHashMap.class);
        LinkedHashMap<String, Map<String, Object>> defaultKeyStatistics = (LinkedHashMap) ((LinkedHashMap) ((ArrayList) ((LinkedHashMap) json.get("quoteSummary"))
                .get("result"))
                .get(0))
                .get("defaultKeyStatistics");


        Map<String, String> parameters = new HashMap<>();

        for (Map.Entry<String, Map<String, Object>> entry : defaultKeyStatistics.entrySet()) {
            if (entry.getValue() instanceof LinkedHashMap) {
                String key = entry.getKey();
                Map<String, Object> innerMap = entry.getValue();
                String fmtValue = (String) innerMap.get("fmt");
                parameters.put(key, fmtValue);
            }
        }

return parameters;
    }
}