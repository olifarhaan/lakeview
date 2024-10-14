package com.olifarhaan.request;

import com.olifarhaan.util.Constants;
import com.olifarhaan.validation.ValidEmail;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class LoginRequest {
	@NotEmpty
	@ValidEmail
	private String email;
	
	@NotEmpty
	@Pattern(regexp = Constants.PASSWORD_REGEX, message = Constants.INVALID_PASSWORD_MESSAGE)
	private String password;

}
