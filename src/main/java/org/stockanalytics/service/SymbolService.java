package org.stockanalytics.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.stockanalytics.dao.SymbolRepository;
import org.stockanalytics.dto.SymbolDto;
import org.stockanalytics.dto.YfSymbolDto;
import org.stockanalytics.dto.YfSymbolListDto;
import org.stockanalytics.model.Symbol;

import java.util.List;
import java.util.stream.Collectors;

@Service
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
        String SYMBOL_API_URL = "https://query2.finance.yahoo.com/v1/finance/search?q=%s";
        if (symbolRepository.existsById(symbolName))
            return null;
        RestTemplate restTemplate = new RestTemplate();

        YfSymbolListDto yfSymbolListDto;
        try {
            yfSymbolListDto = restTemplate.getForObject(String.format(SYMBOL_API_URL, symbolName), YfSymbolListDto.class);
        } catch (HttpClientErrorException e) {
            return null;
        }
        YfSymbolDto yfSymbolDto = yfSymbolListDto.getQuotes().stream()
                .filter(q -> symbolName.equals(q.getSymbol()))
                .findAny()
                .orElseThrow();
        Symbol newSymbol = yfSymbolDto == null ? null : Symbol.builder()
                .name(yfSymbolDto.getSymbol())
                .exchange(yfSymbolDto.getExchange())
                .industryCategory(yfSymbolDto.getIndustry())
                .companyName(yfSymbolDto.getLongname())
                .type(yfSymbolDto.getTypeDisp())
                .status(false)
                .build();
        return newSymbol == null ? null : symbolRepository.save(newSymbol);
    }

}
