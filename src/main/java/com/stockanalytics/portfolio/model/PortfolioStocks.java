package com.stockanalytics.portfolio.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class PortfolioStocks implements Serializable {

    @Serial
    private static final long serialVersionUID = -5562936497492387081L;

    private String symbolName;
    private String companyName;
    private Double close;
    private Integer amountOfStocksForUserPortfolio;
    private Double sumOfAmountOfStocks;
}
