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

    //    @OneToMany
    @ManyToOne
//    @JoinColumn(name = "symbol_name")
    Symbol symbol;
        LocalDate date;
        Double dividendRate;
    }
