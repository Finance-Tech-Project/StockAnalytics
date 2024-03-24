package com.stockanalytics.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class VolatilityDto implements Comparable<VolatilityDto>{
    LocalDate time;
    BigDecimal value;

    @Override
    public int compareTo(VolatilityDto o) {
        return this.time.compareTo(o.getTime());
    }
}
