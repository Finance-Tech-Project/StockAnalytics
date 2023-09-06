package org.stockanalytics.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.stockanalytics.dto.SymbolDto;
import org.stockanalytics.service.SymbolService;

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
    }
