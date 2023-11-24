package com.stockanalytics.portfolio.service.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@ResponseStatus(HttpStatus.NOT_FOUND)
public class SymbolNotInWatchlistException extends RuntimeException {



}
