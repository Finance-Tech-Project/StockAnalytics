package telran.fintech.accounting.model;

import java.util.HashSet;

import java.util.Set;

import org.hibernate.annotations.Table;

//import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@AllArgsConstructor

@Getter
@Entity
//@Document(collection = "FIN_TechUsers")
public class UserAccount {
	@Id
private	String login;
	@Setter
	private	String email;
	@Setter
	private	String password;
	@Setter
	private	String firstName;
	@Setter
	private String lastName;
	Set<String> roles;
	
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
