package com.stockanalytics.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;

@Component
@RequiredArgsConstructor
public class QuoteDataRounding {
    final DecimalFormat df = new DecimalFormat("#.##");

    public double round(double number) {
        String str = df.format(number).replace(",", ".");
        return Double.parseDouble(str);
    }
}
