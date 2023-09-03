package org.stockanalytics.service;

import org.stockanalytics.dto.SymbolDto;

import java.util.List;

public interface SymbolService {

    int addSymbolsFromList(List<String> symbolNames);

    List<SymbolDto> findAllSymbols();

}
