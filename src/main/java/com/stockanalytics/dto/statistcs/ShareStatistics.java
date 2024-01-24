package com.stockanalytics.dto.statistcs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShareStatistics{
    String sharesOutstanding;
    String impliedSharesOutstanding;
    String floatShares;
    String heldPercentInsiders;
    String heldPercentInstitutions;
    String sharesShort;
    String shortRatio;
    String shortPercentOfFloat;
    String sharesPercentSharesOut;
    String sharesShortPrevMonth;
    String sharesShortPreviousMonthDate;
    String averageDailyVolume3Month;
    String averageDailyVolume10Day;
    String averageVolume10days;
}
