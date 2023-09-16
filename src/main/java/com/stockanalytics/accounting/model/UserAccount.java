package com.stockanalytics.accounting.model;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.Id;
//import org.hibernate.annotations.Table;
import org.springframework.data.mongodb.core.mapping.Document;

//import com.stockanalytics.portfolio.model.Portfolio;

//import org.springframework.data.mongodb.core.mapping.Document;
//import javax.persistence.*;
//import jakarta.persistence.EmbeddedId;
//import jakarta.persistence.Entity;
//import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
@AllArgsConstructor
@Getter
//@Entity
@EqualsAndHashCode
@Document(collection = "FIN_TechUsers")
public class UserAccount implements Serializable{
	
	private static final long serialVersionUID = -6631032945500720346L;
	@Id
	String login;
	@Setter
		String email;
	@Setter
		String password;
	@Setter
		String firstName;
	@Setter
	 String lastName;
//	@ElementCollection
Set<String> roles;
//	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL) // Добавьте это поле для связи с портфолио
//    private List<Portfolio> portfolios;
	public UserAccount() {
		roles = new HashSet<>();
	}

	public UserAccount(String login, String password, String firstName, String lastName) {
		this();
		this.login = login;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email=email;
	}

	public boolean addRole(String role) {
		return roles.add(role);
	}

	public boolean removeRole(String role) {
		return roles.remove(role);
	}

}
