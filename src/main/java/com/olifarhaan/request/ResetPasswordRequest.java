package com.olifarhaan.request;

import com.olifarhaan.util.Constants;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class ResetPasswordRequest {
	@NotEmpty
	@Pattern(regexp = Constants.PASSWORD_REGEX, message = Constants.INVALID_PASSWORD_MESSAGE)
	private String password;
	
	@NotEmpty
	private String token;
}
