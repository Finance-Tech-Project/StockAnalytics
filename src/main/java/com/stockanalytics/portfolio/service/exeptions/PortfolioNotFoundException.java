package com.stockanalytics.portfolio.service.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PortfolioNotFoundException  extends  RuntimeException {
    private static final long serialVersionUID = 1l;
//    public PortfolioNotFoundException(String message) {
//        super(message); // Передаем сообщение об ошибке в конструктор суперкласса
//    }
}