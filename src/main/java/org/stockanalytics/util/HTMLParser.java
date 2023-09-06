package org.stockanalytics.util;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.stockanalytics.dto.StockQuoteDto;


import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class HTMLParser {
    public String ticker;
    public LocalDate dateFrom;
    public LocalDate dateTo;
    public String interval;
    public String filter;
    public String frequency;
    public String includeAdjustedClose;

    public HTMLParser(String ticker, LocalDate dateFrom, LocalDate dateTo, String interval) {
        this.ticker = ticker.contains("^")? ticker.replace("^", "%5E") : ticker;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.interval = "1d";
       }

    public HTMLParser() {

    }
//    String start = String.valueOf(dateFrom.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli());
//    String start = dateToSeconds (dateFrom);
//    String end = String.valueOf(dateTo.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli());
//    String end = dateToSeconds (dateTo);

    public List<StockQuoteDto> getDaylyHistoryByPeriod() throws  IOException {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH);

        Document doc = Jsoup.connect("https://finance.yahoo.com/quote/AAPL/history?period1=978307200&period2=1691625600&interval=1d&filter=history&frequency=1d&includeAdjustedClose=true").get();
        Element table = doc.select("table").first(); //находим первую таблицу в документе, если надо вторую, третью или т.д. используем .get(номер)
        Elements rows = null;
        if (table != null) {
            rows = table.select("tr");
        }
        List<List<StockQuoteDto>> data = new ArrayList<>();
        List<StockQuoteDto> daylyData = new ArrayList<>();
        if (rows != null) {
            for (int i = 1; i < rows.size()-1 ; i++) {
                Element row = rows.get(i);
                if(row.text().contains("Dividend"))break;
                Elements cols = row.select("td");
                List<String> quote = (cols.stream()
//                        .filter(col -> col.text().contains("Dividend"))
                        .map(col -> col.text().replace(",",""))
//                        .map(col -> col.replace(".",","))
                        .collect(Collectors.toList()));
                StockQuoteDto stockQuoteDto = new StockQuoteDto(LocalDate.parse(quote.get(0), formatter), Double.valueOf(quote.get(1)),
                        Double.valueOf(quote.get(2)), Double.valueOf(quote.get(3)), Double.valueOf(quote.get(4)), Long.valueOf(quote.get(6)));
                daylyData.add(stockQuoteDto);
            }
        } return daylyData;
    }

    private String dateToSeconds (LocalDate date){
        Instant instant = date.atStartOfDay(ZoneId.systemDefault()).toInstant().plusSeconds(10800);
        long seconds = instant.getEpochSecond();
        return Long.toString(seconds);
    }
}