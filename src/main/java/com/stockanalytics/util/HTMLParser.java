package com.stockanalytics.util;


import com.stockanalytics.dto.StockQuoteDto;
import com.stockanalytics.model.Dividend;
import com.stockanalytics.model.Symbol;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
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

    public List<StockQuoteDto> getDividendByPeriod() throws  IOException {
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

    public List<Dividend> getDividend(Symbol symbol) {
        String url1 = "https://finance.yahoo.com/quote/".concat(symbol.getName().concat("/history?period1=978307200&period2=1356912000&interval=3mo&filter=history&frequency=1d&includeAdjustedClose=true"));
        String url2 = "https://finance.yahoo.com/quote/".concat(symbol.getName().concat("/history?period1=1356998400&period2=1735603200&interval=3mo&filter=history&frequency=1d&includeAdjustedClose=true"));
        List<Dividend> dividends = new ArrayList<>();
        getDividendFromTable(url1 , dividends, symbol);
        getDividendFromTable(url2 , dividends, symbol);

        if (dividends.size() == 0) {
            symbol.setHasDividends(1);
        }
        if (dividends.size() != 0) {
            symbol.setHasDividends(2);
        }

        return dividends.stream().sorted(Comparator.comparing(Dividend::getDate))
                .collect(Collectors.toList());
    }

    private void  getDividendFromTable(String url, List<Dividend> dividends, Symbol symbol){
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH);
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Element table = null;
        if (doc != null) {
            table = doc.select("table").first();
        }
        Elements rows = null;
        if (table != null) {
            rows = table.select("tr");
        }

        if (rows != null) {
            for (int i = 1; i < rows.size()-1 ; i++) {
                Element row = rows.get(i);
                if(row.text().contains("Dividend")) {
                    Elements cols = row.select("td");
                    Element dateElement = cols.select("td:nth-of-type(1) span").first();
                    LocalDate date = null;
                    if (dateElement != null) {
                        date = LocalDate.parse(dateElement.text(),formatter);
                    }
                    Element dividendElement = row.select("td:nth-of-type(2) strong").first();
                    Double dividendRate = null;
                    if (dividendElement != null) {
                        dividendRate = Double.parseDouble(dividendElement.text());
                    }

                    Dividend dividend = new Dividend();
                    dividend.setDate(date);
                    dividend.setDividendRate(dividendRate);
                    dividend.setSymbol(symbol);

                    dividends.add(dividend);
                }
            }
        }
    }
}