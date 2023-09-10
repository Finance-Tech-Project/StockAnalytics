package com.stockanalytics.model;


import lombok.*;

import javax.persistence.*;
import java.util.Map;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor

@Getter
@Setter
public class Statistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "symbol_id")
    Symbol symbol;

    @ElementCollection
    @CollectionTable(name = "statistics_mapValuationMeasures")
    @MapKeyColumn(name = "mapValuationMeasures_key")
    @Column(name = "mapValuationMeasures_value")

    Map<String, String> valuationMeasures;

    @ElementCollection
    @CollectionTable(name = "map_field1", joinColumns = @JoinColumn(name = "entity_id"))
    @MapKeyColumn(name = "map_key")
    @Column(name = "map_value")
    Map<String, String> profitability;

    @ElementCollection
    @CollectionTable(name = "map_field1", joinColumns = @JoinColumn(name = "entity_id"))
    @MapKeyColumn(name = "map_key")
    @Column(name = "map_value")
    Map<String, String> stockPriceHistory;

    @ElementCollection
    @CollectionTable(name = "map_field1", joinColumns = @JoinColumn(name = "entity_id"))
    @MapKeyColumn(name = "map_key")
    @Column(name = "map_value")
    Map<String, String> shareStatistics;

    @ElementCollection
    @CollectionTable(name = "map_field1", joinColumns = @JoinColumn(name = "entity_id"))
    @MapKeyColumn(name = "map_key")
    @Column(name = "map_value")
    Map<String, String> incomeStatement;

    @ElementCollection
    @CollectionTable(name = "map_field1", joinColumns = @JoinColumn(name = "entity_id"))
    @MapKeyColumn(name = "map_key")
    @Column(name = "map_value")
    Map<String, String> balanceSheet;

    @ElementCollection
    @CollectionTable(name = "map_field1", joinColumns = @JoinColumn(name = "entity_id"))
    @MapKeyColumn(name = "map_key")
    @Column(name = "map_value")
    Map<String, String> cashFlowStatement;

    @ElementCollection
    @CollectionTable(name = "map_field1", joinColumns = @JoinColumn(name = "entity_id"))
    @MapKeyColumn(name = "map_key")
    @Column(name = "map_value")
    Map<String, String> dividendsAndSplits;

    @ElementCollection
    @CollectionTable(name = "map_field1", joinColumns = @JoinColumn(name = "entity_id"))
    @MapKeyColumn(name = "map_key")
    @Column(name = "map_value")
    Map<String, String> fiscalYear;

}

