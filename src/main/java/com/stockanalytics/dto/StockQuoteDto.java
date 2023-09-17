package com.stockanalytics.dto;


import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import com.stockanalytics.util.LocalDateConverter;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class StockQuoteDto {
//    String symbol;
@CsvCustomBindByName(column = "date", converter = LocalDateConverter.class)
    LocalDate date;
    @CsvBindByName(column = "open")
    Double open;
    @CsvBindByName(column = "high")
    Double high;
    @CsvBindByName(column = "low")
    Double low;

    @CsvBindByName(column = "close")
    Double close;
    @CsvBindByName(column = "volume")
    Long volume;


}
