package com.stockanalytics.portfolio.service.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class StocksNotFoundException extends RuntimeException {

}
