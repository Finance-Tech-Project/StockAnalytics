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
//    String ytdReturn;
//    String yield;
//
//    public boolean containsField(String fieldName) {
//        return "sharesOutstanding".equals(fieldName) || "impliedSharesOutstanding".equals(fieldName);
//    }
//
//    public void setField(String fieldName, String value) {
//        if ("sharesOutstanding".equals(fieldName)) {
//            this.sharesOutstanding = value;
//        } else if ("impliedSharesOutstanding".equals(fieldName)) {
//            this.impliedSharesOutstanding = value;
//        }
//    }
}
