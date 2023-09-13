package com.stockanalytics.dto.statistcs;


import lombok.Getter;
import lombok.Setter;

//@Entity
//@NoArgsConstructor
//@AllArgsConstructor
@Getter
@Setter
//@Table(name ="stock_price_history")
public class StockPriceHistory{

//    @GeneratedValue(strategy = GenerationType.AUTO)
//    @Column(name = "id", nullable = false)
//    private Long id;
//    @Id
//    @OneToOne
//    @JoinColumn(name = "statistics_id")
//    Statistics statistics;
//
    String beta;
    String beta3Year;
    String SandP52WeekChange;
    String FiftyTowWeekChange;
    String FiftyTowWeekHigh;
    String FiftyTowWeekLow;
    String FiftyTowWeekSPChange;
    String FiftyDaysMovingAverage;
    String twoHundredDaysMovingAverage;
//
//
//    public boolean containsField(String fieldName) {
//        return "beta".equals(fieldName) || "beta3Year".equals(fieldName);
//    }
//
//    public void setField(String fieldName, String value) {
//        if ("beta".equals(fieldName)) {
//            this.beta = value;
//        } else if ("beta3Year".equals(fieldName)) {
//            this.beta3Year = value;
//        }
//    }
}
