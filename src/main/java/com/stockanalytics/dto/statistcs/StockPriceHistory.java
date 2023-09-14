package com.stockanalytics.dto.statistcs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class StockPriceHistory{
    String beta;
    String beta3Year;
    String SandP52WeekChange;
    String FiftyTowWeekChange;
    String FiftyTowWeekHigh;
    String FiftyTowWeekLow;
    String FiftyDaysMovingAverage;
    String twoHundredDaysMovingAverage;
}
