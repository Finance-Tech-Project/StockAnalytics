package org.stockanalytics.dto;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SymbolDto {
    String name;
    String companyName;
    String exchange;
    String industryCategory;
    String type;
}
