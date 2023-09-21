package com.stockanalytics.util;

import com.opencsv.bean.AbstractBeanField;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateConverter  extends AbstractBeanField<LocalDate, String> {

    @Override
    protected LocalDate convert(String value) {
         return LocalDate.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
