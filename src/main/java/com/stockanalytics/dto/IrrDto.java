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
public class IrrDto {
    @CsvCustomBindByName(column = "date", converter = LocalDateConverter.class)
    LocalDate date;

    @CsvBindByName(column = "value")
    Double value;
}
