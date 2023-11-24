package com.stockanalytics.model;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
