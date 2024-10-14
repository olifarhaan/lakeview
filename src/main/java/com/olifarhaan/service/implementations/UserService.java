package com.olifarhaan.service.implementations;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.olifarhaan.model.User;
import com.olifarhaan.repository.UserRepository;
import com.olifarhaan.request.LoginRequest;
import com.olifarhaan.request.ResetPasswordRequest;
import com.olifarhaan.request.UserRegistrationRequest;
import com.olifarhaan.request.UserUpdateRequest;
import com.olifarhaan.response.AuthResponse;
import com.olifarhaan.security.JwtUtils;
import com.olifarhaan.security.JwtUtils.TokenClaim;
import com.olifarhaan.service.interfaces.IUserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final EmailService emailService;

    @Override
    public User createUser(UserRegistrationRequest request, String encodedPassword) {
        return userRepository.save(new User(request, encodedPassword));
    }

    @Override
    public AuthResponse authenticateUser(LoginRequest request, AuthenticationManager authenticationManager) {
        User user = findUserByEmail(request.getEmail());
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getId(), request.getPassword()));
        String jwt = jwtUtils.generateJwtTokenForUser(authentication);
        return new AuthResponse(jwt, user);
    }

    @Override
    public User findUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User with id %s not found".formatted(userId)));
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email %s not found".formatted(email)));
    }

    /*
     * The token is generated with the user's email and first 10 characters of the
     * password. This is to prevent the password from being exposed & serves our
     * purpose as well.
     * If multiple tokens are requested by user at different time,
     * and the password is changed from any one of them,
     * then all other tokens will automatically become invalid since the token
     * contains first few characters of the hashed password as well
     */
    @Override
    public void sendPasswordResetEmail(String email) {
        User user = findUserByEmail(email);
        String resetToken = jwtUtils.generateJwtTokenForPasswordReset(user.getEmail(), user.getPassword());
        emailService.sendPasswordResetEmail(user.getEmail(), resetToken);
    }

    @Override
    public void resetPassword(ResetPasswordRequest resetPasswordRequest, String newEncodedPassword) {
        DecodedJWT jwt = jwtUtils.validatePasswordResetTokenAndGetDecodedJWT(resetPasswordRequest.getToken());
        String email = jwt.getClaim(TokenClaim.EMAIL.name()).asString();
        User user = findUserByEmail(email);
        if (!user.getPassword().substring(0, 10)
                .equals(jwt.getClaim(TokenClaim.PASSWORD_SUBSTRING.name()).asString().substring(0, 10))) {
            throw new SecurityException("Invalid password reset token");
        }
        user.setPassword(newEncodedPassword);
        userRepository.save(user);
    }

    @Override
    public User updateUser(String userId, UserUpdateRequest request) {
        User user = findUserById(userId);
        Optional.ofNullable(request.getFullName()).ifPresent(user::setFullName);
        Optional.ofNullable(request.getEmail()).ifPresent(user::setEmail);
        Optional.ofNullable(request.getGender()).ifPresent(user::setGender);
        Optional.ofNullable(request.getPhoneNumber()).ifPresent(user::setPhoneNumber);
        Optional.ofNullable(request.getAddress()).ifPresent(user::setAddress);
        Optional.ofNullable(request.getDateOfBirth()).ifPresent(user::setDateOfBirth);
        return userRepository.save(user);
    }

    @Override
    public void deleteUserById(String userId) {
        userRepository.deleteById(userId);
    }
}
