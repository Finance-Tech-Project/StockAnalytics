package org.stockanalytics.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class YfSymbolListDto {
    private List<YfSymbolDto> quotes;

}
