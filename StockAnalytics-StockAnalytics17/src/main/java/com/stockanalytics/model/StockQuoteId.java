package com.stockanalytics.model;

import lombok.*;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@EqualsAndHashCode(of = {"date", "symbol"})
public class StockQuoteId implements Serializable {

    @Column(nullable = false)
    LocalDate date;
    @ManyToOne
    Symbol symbol;
}