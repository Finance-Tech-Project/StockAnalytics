package com.stockanalytics.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.stockanalytics.dto.SymbolDto;
import com.stockanalytics.service.SymbolService;

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
