package com.olifarhaan.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.olifarhaan.model.User;
import com.olifarhaan.request.LoginRequest;
import com.olifarhaan.request.ResetPasswordRequest;
import com.olifarhaan.request.UserRegistrationRequest;
import com.olifarhaan.response.MessageResponse;
import com.olifarhaan.service.interfaces.IUserService;

import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Authentication APIs", description = "APIs for user authentication")
@SecurityRequirements({})
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
	private final IUserService userService;

	/*
	 * Sending the auth manager to the service layer
	 * to break circular dependency
	 */
	private final AuthenticationManager authenticationManager;
	private final PasswordEncoder passwordEncoder;

	@PostMapping("/signup")
	public ResponseEntity<User> registerUser(@Valid @RequestBody UserRegistrationRequest request) {
		User savedUser = userService.createUser(request, passwordEncoder.encode(request.getPassword()));
		return ResponseEntity.ok(savedUser);
	}

	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest request) {
		return ResponseEntity.ok(userService.authenticateUser(request, authenticationManager));
	}

	@PostMapping("/forgot-password")
	public ResponseEntity<MessageResponse> sendResetPasswordEmail(@RequestParam String email) {
		userService.sendPasswordResetEmail(email);
		return ResponseEntity.ok(new MessageResponse("Password reset email sent successfully."));
	}

	@PostMapping("/reset-password")
	public ResponseEntity<MessageResponse> resetPassword(
			@RequestBody @Valid ResetPasswordRequest resetPasswordRequest) {
		userService.resetPassword(resetPasswordRequest, passwordEncoder.encode(resetPasswordRequest.getPassword()));
		return ResponseEntity.ok(new MessageResponse("Password reset successfully."));
	}

}
