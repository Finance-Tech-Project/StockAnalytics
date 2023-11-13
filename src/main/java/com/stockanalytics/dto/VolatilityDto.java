package com.stockanalytics.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class VolatilityDto {
    LocalDate time;
    BigDecimal value;
}
