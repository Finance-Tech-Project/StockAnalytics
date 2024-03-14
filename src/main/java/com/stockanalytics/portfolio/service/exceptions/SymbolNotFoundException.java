package com.stockanalytics.portfolio.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SymbolNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -2338626292552177485L;

}
