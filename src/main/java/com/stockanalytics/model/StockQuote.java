package com.stockanalytics.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class StockQuote {
    @EmbeddedId
    StockQuoteId id;
    Double open;
    Double high;
    Double low;
    Double close;
    Long volume;

    public LocalDate getDate() {
        if (getId() != null){
            return getId().getDate();
    }
    return null;
}


}
