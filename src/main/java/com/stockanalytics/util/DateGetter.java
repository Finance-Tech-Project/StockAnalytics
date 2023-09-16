//package com.stockanalytics.util;
//
//
//import com.opencsv.CSVReader;
//import com.opencsv.bean.CsvToBean;
//import com.opencsv.bean.CsvToBeanBuilder;
//import lombok.AllArgsConstructor;
//import lombok.Setter;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.client.RestTemplate;
//import com.stockanalytics.dto.StockQuoteDto;
//import com.stockanalytics.model.Symbol;
//
//import java.io.StringReader;
//import java.time.LocalDate;
//import java.time.ZoneId;
//import java.time.ZoneOffset;
//import java.time.ZonedDateTime;
//import java.util.List;
//@AllArgsConstructor
//@Setter
//public class DateGetter {
//    private final RestTemplate restTemplate = new RestTemplate();
//
//
//
//    public List<StockQuoteDto> getHistoryStockQuotes(LocalDate startDate, LocalDate endDate, Symbol symbol) {
//        ZoneId newYorkZone = ZoneId.of("America/New_York");
//        ZonedDateTime currentTimeInNewYork = ZonedDateTime.now(newYorkZone);
//        ZoneOffset offset = currentTimeInNewYork.getOffset();
//        long startTimestamp = startDate.atStartOfDay().toInstant(offset).getEpochSecond();
//        long endTimestamp = endDate.atStartOfDay().toInstant(offset).getEpochSecond();
//        String ticker = symbol.getName();
//        String BASE_URL = "https://query1.finance.yahoo.com/v7/finance/download/%s";
//        String urlString = String.format(BASE_URL, ticker) +
//                "?period1=" + startTimestamp +
//                "&period2=" + endTimestamp +
//                "&interval=" + "1d" +
//                "&events=" +  "history" +
//                "&includeAdjustedClose=" + "true";
//
//       System.out.println(urlString);
//        ResponseEntity<String> response = restTemplate.exchange(
//                urlString,
//                HttpMethod.GET,
//                null,
//                String.class
//        );
//
//        String csvData = response.getBody();
//        symbol.setStatus(1);
//        CSVReader csvReader = new CSVReader(new StringReader(csvData));
//        CsvToBean<StockQuoteDto> csvToBean = new CsvToBeanBuilder<StockQuoteDto>(csvReader)
//                .withType(StockQuoteDto.class)
//                .withSeparator(',')
//                .build();
//
//        return csvToBean.parse();
//    }
//
//    public List<StockQuoteDto> getAllHistoryStockQuotes(Symbol symbol){
//        return getHistoryStockQuotes(LocalDate.of(2001,1,1), LocalDate.now().minusDays(1), symbol);
//    }
//}
