package org.stockanalytics.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.stockanalytics.dto.SymbolDto;
import org.stockanalytics.service.SymbolService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SymbolController {

    private final SymbolService symbolService;

    @PostMapping(path = "/addsymbols")
    public int addSymbolsFromList(@RequestBody List<String> symbolNames) {
        return symbolService.addSymbolsFromList(symbolNames);
    }

    @GetMapping(path = "/allsymbols")
    public List<SymbolDto> findAllSymbols() {
        return symbolService.findAllSymbols();
    }

}
