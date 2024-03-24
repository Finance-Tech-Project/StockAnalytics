package com.stockanalytics.dto;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import com.stockanalytics.util.LocalDateConverter;

import java.time.LocalDate;

public class DividendDto {
    @CsvCustomBindByName(column = "date", converter = LocalDateConverter.class)
    LocalDate date;
    @CsvBindByName(column = "dividendRate")
    Double dividendRate;
}
