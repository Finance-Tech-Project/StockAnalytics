package com.stockanalytics.accounting.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class TemporaryPassword {
    @Id
    @Column(name = "login")
    private String login;
    private String temporaryPassword;

}
