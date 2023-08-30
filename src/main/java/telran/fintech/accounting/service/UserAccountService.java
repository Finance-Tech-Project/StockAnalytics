package telran.fintech.accounting.service;

import telran.fintech.accounting.dto.RolesDto;
import telran.fintech.accounting.dto.UserDto;
import telran.fintech.accounting.dto.UserEditDto;
import telran.fintech.accounting.dto.UserRegisterDto;

public interface UserAccountService {

	UserDto register(UserRegisterDto userRegisterDto);

	UserDto getUser(String login);

	UserDto removeUser(String login);

	UserDto updateUser(String login, UserEditDto userEditDto);

	RolesDto changeRolesList(String login, String role, boolean isAddRole);

	void changePassword(String login, String newPassword);

}
