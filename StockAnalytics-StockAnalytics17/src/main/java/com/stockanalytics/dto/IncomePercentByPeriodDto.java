package com.stockanalytics.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class IncomePercentByPeriodDto implements Comparable<IncomePercentByPeriodDto>{
    LocalDate time;
    BigDecimal value;

    @Override
    public int compareTo(IncomePercentByPeriodDto o) {
        return this.time.compareTo(o.getTime());
    }
}
