package com.stockanalytics.dto.statistcs;


import lombok.Getter;
import lombok.Setter;

//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import javax.persistence.*;
//
//
//@Entity
//@NoArgsConstructor
//@AllArgsConstructor
@Getter
@Setter
public class ValuationMeasures {

//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE)
//    @Column(name = "id", nullable = false)
//    private Long id;
//
//    @OneToOne
//    @JoinColumn(name = "statistics_id")
//    Statistics statistics;
//
    String marketCap;
    String bookValue;
    String enterpriseValue;
    String forwardPE;
    String trailingPE;
    String pegRatio;
    String priceToSales;
    String priceToBook;
    String enterpriseToRevenue;
    String enterpriseToEbitda;
    String epsCurrentYear;
    String forwardEps;
    String trailingEps;
    String quickRatio;
//
//    public boolean containsField(String fieldName) {
//        return "marketCap".equals(fieldName)||"bookValue".equals(fieldName)||"enterpriseValue".equals(fieldName)||"forwardPE".equals(fieldName)
//                ||"pegRatio".equals(fieldName)||"priceToSalesTrailing12Months".equals(fieldName)||"priceToBook".equals(fieldName)||
//                "enterpriseToRevenue".equals(fieldName)||"enterpriseToEbitda".equals(fieldName)||"forwardEps".equals(fieldName)||"trailingEps".equals(fieldName);
//    }
//
//    public void setField(String fieldName, String value) {
//         if ("marketCap".equals(fieldName)) {
//            this.marketCap = value;
//        } else if ("bookValue".equals(fieldName)) {
//            this.bookValue = value;
//        }else if ("enterpriseValue".equals(fieldName)) {
//             this.enterpriseValue = value;
//         }else if ("forwardPE".equals(fieldName)) {
//             this.forwardPE = value;
//         }else if ("pegRatio".equals(fieldName)) {
//             this.pegRatio = value;
//         }else if ("priceToSalesTrailing12Months".equals(fieldName)) {
//             this.priceToSalesTrailing12Months = value;
//         }else if ("priceToBook".equals(fieldName)) {
//             this.priceToBook = value;
//         }else if ("enterpriseToRevenue".equals(fieldName)) {
//             this.enterpriseToRevenue = value;
//         }else if ("enterpriseToEbitda".equals(fieldName)) {
//             this.enterpriseToEbitda = value;
//         }else if ("forwardEps".equals(fieldName)) {
//             this.forwardEps = value;
//         }else if ("trailingEps".equals(fieldName)) {
//             this.trailingEps = value;
//         }
//    }
}