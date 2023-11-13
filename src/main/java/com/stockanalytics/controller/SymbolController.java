package com.stockanalytics.controller;

import com.stockanalytics.dto.SymbolDto;
import com.stockanalytics.model.Symbol;
import com.stockanalytics.service.SymbolService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SymbolController {
    private final SymbolService symbolService;

    @PostMapping(path = "/service/addsymbols")
    public int addSymbolsFromList(@RequestBody List<String> symbolNames) {
        return symbolService.addSymbolsFromList(symbolNames);
    }

    @GetMapping(path = "/allsymbols")
    public List<SymbolDto> getAllSymbols() {
        return symbolService.getAllSymbols();
    }

    @GetMapping(path = "/start/symbols")
    public List<Symbol>  getStartSymbols(){
        return symbolService.findStartingSymbols();
        }

    @PostMapping( "/start/add")
    public List<Symbol> addSymbolToStartingPage(@RequestParam String ticker){
        return symbolService.addSymbolToStart(ticker);
        }

    @PostMapping("/start/remove")
    public List<Symbol> removeSymbolFromStartingPage(@RequestParam String ticker){
        return symbolService.removeSymbolFromStart(ticker);
        }

    @GetMapping("/searchSymbol/")
    public List<Symbol> searchSymbolsBySubstring(@RequestParam String search){
        return  symbolService.searchSymbolsBySubstring(search);
    }

}