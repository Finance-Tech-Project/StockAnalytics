package com.stockanalytics.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Dividend {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToOne(optional = false)
    Symbol symbol;
LocalDate date;
    Double dividendRate;
}
