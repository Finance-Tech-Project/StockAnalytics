package org.stockanalytics.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
        return getId().getDate();
    }

}
