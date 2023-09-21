package com.stockanalytics.accounting.model;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//import org.hibernate.annotations.Table;
//import org.springframework.data.mongodb.core.mapping.Document;

//import com.stockanalytics.portfolio.model.Portfolio;

//import org.springframework.data.mongodb.core.mapping.Document;
import javax.persistence.*;
//import jakarta.persistence.EmbeddedId;
//import jakarta.persistence.Entity;
//import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.ElementCollection;

@AllArgsConstructor
@Getter
@Setter
@Entity
@EqualsAndHashCode
public class UserAccount implements Serializable {
	
	private static final long serialVersionUID = -6631032945500720346L;
	@Id
	String login;
	String email;
	String password;
    String firstName;
	String lastName;
	String role;

	public UserAccount() {}
	public UserAccount(String login, String password, String firstName, String lastName) {
		this.login = login;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email=email;
		this.role = "USER";
	}

	public void addRole(String role) {
		this.role = role;
	}

	public void removeRole() {
		this.role = "USER";
	}

}
