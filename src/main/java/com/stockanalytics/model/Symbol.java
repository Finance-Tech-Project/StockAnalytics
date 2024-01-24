package com.stockanalytics.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Symbol implements Serializable {
    @Id
    String name;
    String companyName;
    String exchange;
    String industryCategory;
    String type;
    @Column(nullable = false)
    int isStarting;
    @NotNull
    int status;
    @NotNull
    int hasDividends;
}
