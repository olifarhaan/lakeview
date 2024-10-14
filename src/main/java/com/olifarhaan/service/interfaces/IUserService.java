package com.olifarhaan.service.interfaces;

import org.springframework.security.authentication.AuthenticationManager;

import com.olifarhaan.model.User;
import com.olifarhaan.request.LoginRequest;
import com.olifarhaan.request.ResetPasswordRequest;
import com.olifarhaan.request.UserRegistrationRequest;
import com.olifarhaan.request.UserUpdateRequest;
import com.olifarhaan.response.AuthResponse;

public interface IUserService {
    User createUser(UserRegistrationRequest request, String encodedPassword);

    AuthResponse authenticateUser(LoginRequest request, AuthenticationManager authenticationManager);

    User findUserById(String userId);

    User findUserByEmail(String email);

    void sendPasswordResetEmail(String email);

    User updateUser(String userId, UserUpdateRequest request);

    void resetPassword(ResetPasswordRequest resetPasswordRequest, String encodedPassword);

    void deleteUserById(String userId);
}
