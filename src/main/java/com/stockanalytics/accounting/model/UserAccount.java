package com.stockanalytics.accounting.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@Entity
@EqualsAndHashCode
public class UserAccount implements Serializable {

	@Serial
    private static final long serialVersionUID = -6631032945500720346L;

    @Id
    @Column(name = "login")
    public String login;
    String email;
    String password;
    String firstName;
    String lastName;
    String role;
    @ElementCollection
    @CollectionTable(name = "user_watchlist", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "stock_symbol")
    private List<String> watchlist;

    public UserAccount() {
        this.watchlist = new ArrayList<>();
    }

    public UserAccount(String login, String password, String firstName, String lastName, String email) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = "USER";
        this.watchlist = new ArrayList<>();
    }

    public void addRole(String role) {
        this.role = role;
    }

    public void removeRole() {
        this.role = "USER";
    }

    public void addToWatchList(String symbol) {
        if (watchlist == null) {
            watchlist = new ArrayList<>();
        }
        watchlist.add(symbol);
    }

    public void removeFromWatchList(String symbol) {
        if (watchlist == null) {
            watchlist = new ArrayList<>();
        }
        watchlist.remove(symbol);
    }
}
