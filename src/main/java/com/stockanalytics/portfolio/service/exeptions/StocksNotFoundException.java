package com.stockanalytics.portfolio.service.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serializable;

@ResponseStatus(HttpStatus.CONFLICT)
public class StocksNotFoundException extends RuntimeException  {
    private static final long serialVersionUID = -2338626290552177486L;
}
