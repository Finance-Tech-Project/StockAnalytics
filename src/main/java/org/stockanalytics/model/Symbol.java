package org.stockanalytics.model;

import lombok.*;
import org.springframework.lang.NonNull;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Symbol {
    @Id
    String name;
    String companyName;
    String exchange;
    String industryCategory;
    String type;
    @NonNull
    SymbolStatus status;
}
