package com.stockanalytics.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockanalytics.dto.StockQuoteDto;
import com.stockanalytics.model.Dividend;
import com.stockanalytics.model.Symbol;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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
        ResponseEntity<String> response = restTemplate.exchange(
                urlString,
                HttpMethod.GET,
                null,
                String.class
        );
        String csvData = response.getBody();
        symbol.setStatus(1);
        List<StockQuoteDto> stockQuotes = new ArrayList<>();
        List<Dividend> dividends = new ArrayList<>();

        String[] lines = new String[0];
        if (csvData != null) {
            lines = csvData.split("\n");
        }

        for (String line : lines) {
            String[] values = line.split(",");

                    LocalDate date = LocalDate.parse(values[0]);
                    Double open = Double.parseDouble(values[1]);
                    Double high = Double.parseDouble(values[2]);
                    Double low = Double.parseDouble(values[3]);
                    Double close = Double.parseDouble(values[4]);
                    Long volume = Long.parseLong(values[6]);
                    StockQuoteDto stockQuote = new StockQuoteDto(date, open, high, low, close, volume);
                    stockQuotes.add(stockQuote);
         }
        return stockQuotes;
    }

    public List<StockQuoteDto> getAllHistoryStockQuotes(Symbol symbol){
        return getHistoryStockQuotes(LocalDate.of(2022,1,1), LocalDate.now(), symbol);
    }
    @SuppressWarnings("unchecked")
    public Map <String,Object> getDataForStatisticsFromRapidAPI(Symbol symbol) throws IOException, InterruptedException {
        String ticker = symbol.getName();
        String urlString = String.format("https://apidojo-yahoo-finance-v1.p.rapidapi.com/market/v2/get-quotes?region=US&symbols=%s", ticker);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlString))
                .header("X-RapidAPI-Key", "6dd428f439msh69c2e1ffae0d795p163517jsn07ac2db55933")
                .header("X-RapidAPI-Host", "apidojo-yahoo-finance-v1.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        LinkedHashMap<String, Object> quoteResponse = (LinkedHashMap<String, Object>) objectMapper.readValue(response.body(), LinkedHashMap.class).get("quoteResponse");
        ArrayList< Object> result = (ArrayList<Object>) quoteResponse.get("result");
        return (LinkedHashMap<String, Object>) result.get(0);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Map <String,String> getMapFromRespons(String responseBody, String target) throws JsonProcessingException {
        LinkedHashMap<String, Object> data = (LinkedHashMap) objectMapper.readValue(responseBody, LinkedHashMap.class).get(target);
        LinkedHashMap<String, String> res = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            if (entry.getValue() instanceof LinkedHashMap) {
                String key = entry.getKey();
                HashMap<String, String> innerMap = (HashMap<String, String>) entry.getValue();
                String fmtValue = innerMap.get("fmt");
                res.put(key, fmtValue);
            }
        }
        return res;
    }

    public List<Map<String, String>> getDataForAnalisisFromRapidAPI(Symbol symbol) throws IOException, InterruptedException {
        String ticker = symbol.getName();
        List<Map<String, String>> mapList = new ArrayList<>();
        String urlString = String.format("https://apidojo-yahoo-finance-v1.p.rapidapi.com/stock/v2/get-analysis?symbol=%s&region=US", ticker);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlString))
                .header("X-RapidAPI-Key", "6dd428f439msh69c2e1ffae0d795p163517jsn07ac2db55933")
                .header("X-RapidAPI-Host", "apidojo-yahoo-finance-v1.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        String responseBody = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString()).body();
        mapList.add(getMapFromRespons(responseBody, "financialData"));
        mapList.add(getMapFromRespons(responseBody,  "price"));
        mapList.add(getMapFromRespons(responseBody, "summaryDetail"));
        return mapList;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public Map<String, String> getDataForStatisticsFromYahoo (Symbol symbol) {
        String ticker = symbol.getName();
        String BASE_URL = "https://query1.finance.yahoo.com/v6/finance/quoteSummary/%s?modules=defaultKeyStatistics";
        String urlString = String.format(BASE_URL, ticker);
        LinkedHashMap<String, Map<String, Object>> defaultKeyStatistics;
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    urlString,
                    HttpMethod.GET,
                    null,
                    String.class
            );
            LinkedHashMap<String, Object> json = objectMapper.readValue(response.getBody(), LinkedHashMap.class);
            defaultKeyStatistics = (LinkedHashMap<String, Map<String, Object>>) ((LinkedHashMap)
                    ((ArrayList) ((LinkedHashMap) json.get("quoteSummary")).get("result")).get(0)).get("defaultKeyStatistics");
        } catch (HttpClientErrorException e) {
            return null;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
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