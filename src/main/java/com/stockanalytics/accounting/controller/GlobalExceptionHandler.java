//package com.stockanalytics.accounting.controller;
//
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//@RestControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(CorsException.class)
//    public ResponseEntity<Object> handleYourException(CorsException ex) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Access-Control-Allow-Origin", "http://localhost:3000");
//        // Добавьте другие необходимые заголовки CORS
//        return new ResponseEntity<>(ex.getMessage(), headers, HttpStatus.UNAUTHORIZED);
//    }
//}
