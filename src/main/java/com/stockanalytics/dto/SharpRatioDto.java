package com.stockanalytics.dto;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SharpRatioDto implements Comparable<SharpRatioDto> {
    LocalDate time;
    Double value;

    @Override
    public int compareTo(SharpRatioDto anotherSR) {
        // Сравниваем объекты по полю date
        return this.time.compareTo(anotherSR.getTime());
    }
}
