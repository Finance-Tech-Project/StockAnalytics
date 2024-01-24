package com.stockanalytics.portfolio.service.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("NonAsciiCharacters")
@ResponseStatus(HttpStatus.CONFLICT)
public class StocksNotFoundExсeption extends RuntimeException {

}
