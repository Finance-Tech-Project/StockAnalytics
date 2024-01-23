package com.stockanalytics.accounting.dto.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserExistsException extends RuntimeException {
	@Serial
	private static final long serialVersionUID = 8777376761193864981L;
	public UserExistsException(String login) {
		super("User with login " + login + " already exists");
	}
}
