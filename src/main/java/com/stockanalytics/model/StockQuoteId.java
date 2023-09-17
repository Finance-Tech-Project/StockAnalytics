package com.stockanalytics.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
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