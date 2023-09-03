package org.stockanalytics.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class YfSymbolDto {
    private String symbol;
    private String exchange;
    private String shortname;
    private String longname;
    private String sector;
    private String industry;
    private String typeDisp;
}
