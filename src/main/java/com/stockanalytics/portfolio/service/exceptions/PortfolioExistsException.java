package com.stockanalytics.portfolio.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;
import java.io.Serializable;
@ResponseStatus(HttpStatus.CREATED)
public class PortfolioExistsException extends  RuntimeException implements Serializable {
    @Serial
    private static final long serialVersionUID = -3961116277515959106L;
}
