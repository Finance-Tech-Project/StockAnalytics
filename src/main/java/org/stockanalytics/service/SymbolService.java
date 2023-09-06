package org.stockanalytics.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.stockanalytics.dao.SymbolRepository;
import org.stockanalytics.dto.SymbolDto;
import org.stockanalytics.model.Symbol;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Repository
@RequiredArgsConstructor
public class SymbolService implements SymbolServiceInterface {
    private final SymbolRepository symbolRepository;
    private final ModelMapper modelMapper;

    //    @Override
    public int addSymbolsFromList(List<String> symbolNames) {
        return symbolNames.stream()
                .map(sn -> loadSymbolFromYf(sn) == null ? 0 : 1)
                .reduce(0, Integer::sum);
    }

    //    @Override
    public List<SymbolDto> getAllSymbols() {
        return symbolRepository.findAll().stream()
                .map(t -> modelMapper.map(t, SymbolDto.class))
                .collect(Collectors.toList());
    }

    public Symbol loadSymbolFromYf(String symbolName) {
       if (symbolRepository.existsById(symbolName)){
           return null;
       }
        String SYMBOL_API_URL = "https://query2.finance.yahoo.com/v1/finance/search?q=%s";
        RestTemplate restTemplate = new RestTemplate();
//        @Getter
//        @Setter
//        class YfSymbolDto {
//            private String symbol;
//            private String exchange;
//            private String shortname;
//            private String longname;
//            private String sector;
//            private String industry;
//            private String typeDisp;
//        }
////        @Getter
//        @Setter
//        class YfSymbolListDto {
//            private List<YfSymbolDto> quotes;
//
//        }
//        YfSymbolListDto yfSymbolListDto;
//            try {
//                yfSymbolListDto = restTemplate.getForObject(String.format(SYMBOL_API_URL, symbolName), YfSymbolListDto.class);
//            } catch (HttpClientErrorException e) {
//                return null;
//            }
//            YfSymbolDto yfSymbolDto = yfSymbolListDto.getQuotes().stream()
//                    .filter(q -> symbolName.equals(q.getSymbol()))
//                    .findAny()
//                    .orElseThrow();
        ResponseEntity<String> response = restTemplate.exchange(
                String.format(SYMBOL_API_URL, symbolName),
                HttpMethod.GET,
                null,
                String.class
        );
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(response.getBody(), JsonObject.class);
        JsonArray quotesArray = jsonObject.getAsJsonArray("quotes");
        JsonObject  firstQuote = quotesArray.get(0).getAsJsonObject();


        Symbol symbol = Symbol.builder()
                .name(firstQuote.get("symbol").getAsString())
                .exchange(String.valueOf(firstQuote.get("exchDisp").getAsJsonPrimitive()))
                .industryCategory(firstQuote.get("industryDisp").getAsString())
                .companyName(firstQuote.get("longname").getAsString())
                .type(firstQuote.get("typeDisp").getAsString())
                .status(0)
                .build();
        return symbolRepository.save(symbol);
    }
    public Symbol getSymbol (String ticker){
        Optional<Symbol> opt = symbolRepository.findById(ticker);
        return opt.orElse(null);
    }
}
