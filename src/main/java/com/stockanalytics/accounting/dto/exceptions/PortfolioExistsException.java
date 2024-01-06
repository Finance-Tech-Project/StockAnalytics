package com.stockanalytics.accounting.dto.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class PortfolioExistsException extends RuntimeException {
    private static final long serialVersionUID=123456789L;
}
