//package com.stockanalytics.portfolio.model;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Map;
//
//import javax.persistence.*;
//
//import com.stockanalytics.accounting.model.UserAccount;
//
//import lombok.Getter;
//import lombok.Setter;
//@Getter
//@Entity
//public class Portfolio {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne // Указываем связь многие-к-одному с пользователем
//    @JoinColumn(name = "user_login") // Имя столбца, используемого для связи
//    private UserAccount user;
//
//    private String username;
//    @Setter
//    private List<String> stocks;
//    @Setter
//    private LocalDate portfolioDate;
//    @Setter
//    private int numberOfStocks;
//    @Setter
//    private Map<String, Integer> symbols;
//}
