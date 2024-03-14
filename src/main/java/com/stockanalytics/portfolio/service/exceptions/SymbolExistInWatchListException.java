package com.stockanalytics.portfolio.service.exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.CREATED)
public class SymbolExistInWatchListException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 8327947857082010520L;

    public SymbolExistInWatchListException(String symbol) {
        super("Symbol " + symbol + " already exists in watchlist");
    }
}
