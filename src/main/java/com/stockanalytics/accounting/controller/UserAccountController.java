package com.stockanalytics.accounting.controller;

import java.security.Principal;


import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import com.stockanalytics.accounting.dto.RolesDto;
import com.stockanalytics.accounting.dto.UserDto;
import com.stockanalytics.accounting.dto.UserEditDto;
import com.stockanalytics.accounting.dto.UserRegisterDto;
import com.stockanalytics.accounting.service.UserAccountService;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class UserAccountController {

private  final UserAccountService userAccountService;

	@PostMapping("/register")
	public UserDto register(@RequestBody UserRegisterDto userRegisterDto) {
		return userAccountService.register(userRegisterDto);
	}
	@PostMapping("/login")
	public UserDto login(Principal principal) {
		return userAccountService.getUser(principal.getName());
	}
	@PostMapping("/logout")
	public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			String username = auth.getName(); // Getting the user login
			new SecurityContextLogoutHandler().logout(request, response, auth);
			return ResponseEntity.ok("User " + username + " logged out successfully");
		}
		return ResponseEntity.ok("No user to log out");
	}

	@PostMapping("/recovery/{login}")
    public void forgotPassword(@PathVariable String login) {
        userAccountService.sendTemporaryPassword(login);
    }

	@GetMapping("/user/{login}")
	public UserDto getUser(@PathVariable String login) {

		return userAccountService.getUser(login);
	}

	@DeleteMapping("/removeUser/{login}")
	public UserDto removeUser(@PathVariable String login) {
		return userAccountService.removeUser(login);
	}

	@PutMapping("/user/update/{login}")
	public UserDto updateUser(@PathVariable String login, @RequestBody UserEditDto userEditDto) {
		return userAccountService.updateUser(login, userEditDto);
	}
	

	@PutMapping("/user/{login}/role/{role}")
	public RolesDto addRole(@PathVariable String login, @PathVariable String role) {
		return userAccountService.changeRolesList(login, role, true);
	}

	@DeleteMapping("/user/{login}/role/{role}")
	public RolesDto deleteRole(@PathVariable String login, @PathVariable String role) {
		return userAccountService.changeRolesList(login, role, false);
	}

	@PutMapping("/password")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void changePassword(@NonNull Principal principal, @RequestHeader("X-Password") String newPassword) {
		userAccountService.changePassword(principal.getName(), newPassword);

	}

	@GetMapping("/recovery/{login}")
	public String getPasswordLink(@PathVariable String login) {
		return userAccountService.getPasswordLink(login);
	}
	

}
