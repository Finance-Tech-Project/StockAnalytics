package com.stockanalytics.portfolio.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.CONFLICT)
public class StocksNotFoundException extends RuntimeException  {
    @Serial
    private static final long serialVersionUID = -2338626290552177486L;
}
