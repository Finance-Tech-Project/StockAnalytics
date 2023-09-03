package telran.fintech.accounting.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserRegisterDto {
	
    
	String login;
	@NotBlank(message = "Логин не может быть пустым")
	@Email(message = "Некорректный формат email")
	String email;
    String password;
    String firstName;
    String lastName;
}
