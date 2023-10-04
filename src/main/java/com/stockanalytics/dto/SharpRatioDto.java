package com.stockanalytics.dto;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SharpRatioDto {
    LocalDate time;
    Double value;
}
