package com.stockanalytics.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.stockanalytics.dao.SymbolRepository;
import com.stockanalytics.dto.SymbolDto;
import com.stockanalytics.model.Symbol;
import com.stockanalytics.util.DataGetter;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Repository
@RequiredArgsConstructor
public class SymbolService {
    private final SymbolRepository symbolRepository;
    private final StockQuoteService stockQuoteService;
    private final ModelMapper modelMapper;
    private final DataGetter getter;


    public int addSymbolsFromList(List<String> symbolNames) {
        return symbolNames.stream()
                .map(sn -> {
                    try {
                        return loadSymbolFromYf(sn) == null ? 0 : 1;
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                })
                .reduce(0, Integer::sum);
    }

    public List<SymbolDto> getAllSymbols() {
        return symbolRepository.findAll().stream()
                .map(t -> modelMapper.map(t, SymbolDto.class))
                .collect(Collectors.toList());
    }

    public Symbol loadSymbolFromYf(String symbolName) throws InterruptedException {
        if (symbolRepository.existsById(symbolName)) {
            return null;
        }
        Thread.sleep(1000);
        String SYMBOL_API_URL = "https://query2.finance.yahoo.com/v1/finance/search?q=%s";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                String.format(SYMBOL_API_URL, symbolName),
                HttpMethod.GET,
                null,
                String.class
        );
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(response.getBody(), JsonObject.class);
        JsonArray quotesArray = jsonObject.getAsJsonArray("quotes");
        for(int i =0 ; i< quotesArray.size();){
            JsonObject quote = quotesArray.get(i).getAsJsonObject();
            if(quote.get("quoteType")!= null && quote.get("quoteType").getAsString().equals("EQUITY") && quote.get("industryDisp")!= null
                    && quote.get("longname") != null) {
           Symbol symbol = Symbol.builder()
                    .name(quote.get("symbol").getAsString())
                    .exchange(String.valueOf(quote.get("exchDisp").getAsString()))
          .industryCategory(String.valueOf(quote.get("industryDisp").getAsString()))
                .companyName(String.valueOf(quote.get("longname").getAsString()))
                .type(String.valueOf(quote.get("typeDisp").getAsString()))
                    .status(0)
                    .isStarting(0)
                    .build();

            symbolRepository.save(symbol);
        }
         i++;
    }
        return null;
    }
    public Symbol getSymbol (String ticker){
        if(symbolRepository.existsById(ticker)) {
            return   symbolRepository.getById(ticker);
        }
        return null;
    }

    public List<Symbol> findStartingSymbols() {

        return  symbolRepository.findAllByIsStartingEquals(1);
    }

    public List<Symbol> addSymbolToStart(String ticker) {
        Symbol symbol = symbolRepository.getByName(ticker);
        symbol.setIsStarting(1);
        stockQuoteService.getData(LocalDate.now().minusDays(1),LocalDate.now(), symbol);
        symbol.setStatus(1);
        symbolRepository.save(symbol);
        return symbolRepository.findAllByIsStartingEquals(1);
    }

    public List<Symbol> removeSymbolFromStart(String ticker){
        Symbol symbol = symbolRepository.getByName(ticker);
        symbol.setIsStarting(0);
        symbolRepository.save(symbol);
        return symbolRepository.findAllByIsStartingEquals(1);
    }
//    public Map getStatistics(Symbol symbol) throws JsonProcessingException {
//        return getter.getDataForStatistics(symbol);
//    }
}
