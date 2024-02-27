package com.stockanalytics.util;


import com.stockanalytics.dto.StockQuoteDto;
import com.stockanalytics.model.Dividend;
import com.stockanalytics.model.Symbol;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@SuppressWarnings({"unused", "CallToPrintStackTrace"})
public class HTMLParser {

    public String ticker;
    public LocalDate dateFrom;
    public LocalDate dateTo;
    public String interval;
    public String filter;
    public String frequency;
    public String includeAdjustedClose;

    public HTMLParser(String ticker, LocalDate dateFrom, LocalDate dateTo, String interval) {
        this.ticker = ticker.contains("^") ? ticker.replace("^", "%5E") : ticker;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.interval = "1d";
    }

    public HTMLParser() {

    }

    public List<StockQuoteDto> getDividendByPeriod() throws IOException {
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
            for (int i = 1; i < rows.size() - 1; i++) {
                Element row = rows.get(i);
                if (row.text().contains("Dividend")) break;
                Elements cols = row.select("td");
                List<String> quote = (cols.stream()
                        .map(col -> col.text().replace(",", ""))
                        .toList());
                StockQuoteDto stockQuoteDto = new StockQuoteDto(LocalDate.parse(quote.get(0), formatter), Double.valueOf(quote.get(1)),
                        Double.valueOf(quote.get(2)), Double.valueOf(quote.get(3)), Double.valueOf(quote.get(4)), Long.valueOf(quote.get(6)));
                daylyData.add(stockQuoteDto);
            }
        }
        return daylyData;
    }

    private String dateToSeconds(LocalDate date) {
        Instant instant = date.atStartOfDay(ZoneId.systemDefault()).toInstant().plusSeconds(10800);
        long seconds = instant.getEpochSecond();
        return Long.toString(seconds);
    }

    public List<Dividend> getDividend(Symbol symbol) {
        List<Dividend> dividends = new ArrayList<>();
        String url = "https://query1.finance.yahoo.com/v7/finance/download/".concat(symbol.getName().concat("?period1=0&period2=3476250418&interval=1d&events=div&includeAdjustedClose=true"));
        dividends = downloadAndProcessDividends(url, symbol);

        if (dividends.isEmpty()) {
            symbol.setHasDividends(0);
        }
        if (!dividends.isEmpty()) {
            symbol.setHasDividends(1);
        }

        return dividends.stream().sorted(Comparator.comparing(Dividend::getDate))
                .collect(Collectors.toList());
    }


    /**
     * Downloads and processes dividend data from a specified URL, parsing it into a list of {@link Dividend} objects.
     * <p>
     * This method connects to the given URL, expected to return a CSV file containing dividend information, with each row
     * representing a dividend payment. The CSV format is assumed to have at least two columns: the payment date and
     * the dividend amount. The method parses each row of the CSV into a {@link Dividend} object, which is then added
     * to a list of dividends.
     * <p>
     * Note: This method requires a stable internet connection and the ability to connect to the external URL. It also assumes
     * that the URL points directly to a downloadable CSV file containing the relevant dividend data.
     *
     * @param urlString The URL from which to download the dividend data, formatted as a string.
     * @return A List of {@link Dividend} objects, each representing a dividend payment as parsed from the downloaded CSV.
     * @throws IOException If an input or output exception occurred during network connection or file processing.
     */
    public static List<Dividend> downloadAndProcessDividends(String urlString, Symbol symbol) {
        List<Dividend> dividends = new ArrayList<>();

        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            // Set User-Agent to mimic a web browser request
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3");
            connection.setRequestProperty("Accept", "text/csv");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                // Assuming the first line is the header and skipping it
                in.readLine();

                while ((inputLine = in.readLine()) != null) {
                    String[] data = inputLine.split(",");
                    LocalDate date = LocalDate.parse(data[0]);
                    Double amount = Double.valueOf(data[1]);

                    Dividend dividend = new Dividend();
                    dividend.setSymbol(symbol);
                    dividend.setDate(date);
                    dividend.setDividendRate(amount);
                    dividends.add(dividend);
                }
                in.close();
            } else {
                System.out.println("Failed to download dividends data: HTTP error code " + responseCode);
            }
        } catch (Exception e) {
            System.err.println("An error occurred while downloading or processing dividends data:");
            e.printStackTrace();
        }

        return dividends;
    }
}