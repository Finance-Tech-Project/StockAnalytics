package com.stockanalytics.accounting.dto;

import java.util.Set;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
	String login;
	String email;
	String firstName;
	String lastName;
	@Singular
	Set<String> roles;
}
