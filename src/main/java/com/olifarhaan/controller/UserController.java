package com.olifarhaan.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.olifarhaan.model.User;
import com.olifarhaan.request.UserUpdateRequest;
import com.olifarhaan.service.interfaces.IUserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "User APIs", description = "APIs for user management")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController extends BaseController {

	private final IUserService userService;

	@GetMapping("/profile")
	public ResponseEntity<User> getUserProfileHandler() {
		return ResponseEntity.ok(getLoggedInUser());
	}

	@GetMapping("/{userId}")
	@PreAuthorize("hasRole('ADMIN') or authentication.name == #userId")
	public ResponseEntity<User> findUserById(@PathVariable String userId) {
		return ResponseEntity.ok(userService.findUserById(userId));
	}

	@PutMapping("/{userId}")
	@PreAuthorize("hasRole('ADMIN') or authentication.name == #userId")
	public ResponseEntity<User> updateUserById(@PathVariable String userId,
			@Valid @RequestBody UserUpdateRequest request) {
		return ResponseEntity.ok(userService.updateUser(userId, request));
	}

	@DeleteMapping("/{userId}")
	@PreAuthorize("hasRole('ADMIN') or authentication.name == #userId")
	public ResponseEntity<Void> deleteUserById(@PathVariable String userId) {
		userService.deleteUserById(userId);
		return ResponseEntity.noContent().build();
	}

}
