package com.stockanalytics.model;

import lombok.*;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode(of = {"date", "symbol"})
public class StockQuoteId implements Serializable {

    @Serial
    private static final long serialVersionUID = 8004042181326097600L;

    @Column(nullable = false)
    LocalDate date;
    @ManyToOne
    Symbol symbol;
}