package com.olifarhaan.security;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;

/**
 * @author M. Ali Farhan
 */

@Component
public class JwtUtils {
    @Value("${auth.token.jwtSecret}")
    private String JWT_SECRET;

    @Value("${auth.token.expirationInMils}")
    private int JWT_EXPIRATION_MS;

    @Value("${auth.token.passwordResetExpirationInMils}")
    private int PASSWORD_RESET_EXPIRATION_MS;

    public static final String BEARER_PREFIX = "Bearer ";

    public enum TokenClaim {
        USER_ID,
        EMAIL,
        ROLES,
        TOKEN_TYPE,
        PASSWORD_SUBSTRING;
    }

    public enum TokenType {
        AUTH_TOKEN,
        PASSWORD_RESET_TOKEN,
        EMAIL_VERIFICATION_TOKEN;
    }

    public String generateJwtTokenForUser(Authentication authentication) {
        AppUserDetails userPrincipal = (AppUserDetails) authentication.getPrincipal();
        List<String> roles = userPrincipal.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority).toList();

        Map<TokenClaim, Object> claims = Map.of(
                TokenClaim.USER_ID, userPrincipal.getUsername(),
                TokenClaim.EMAIL, userPrincipal.getEmail(),
                TokenClaim.ROLES, roles);

        return generateToken(userPrincipal.getUsername(), TokenType.AUTH_TOKEN, JWT_EXPIRATION_MS, claims);
    }

    public String generateJwtTokenForPasswordReset(String email, String password) {
        Map<TokenClaim, Object> claims = Map.of(
                TokenClaim.EMAIL, email,
                TokenClaim.PASSWORD_SUBSTRING, password.substring(0, 10));

        return generateToken(email, TokenType.PASSWORD_RESET_TOKEN, PASSWORD_RESET_EXPIRATION_MS, claims);
    }

    public DecodedJWT validateAuthTokenAndGetDecodedJWT(String token) {
        return validateToken(token, TokenType.AUTH_TOKEN);
    }

    public DecodedJWT validatePasswordResetTokenAndGetDecodedJWT(String token) {

        DecodedJWT jwt = validateToken(token, TokenType.PASSWORD_RESET_TOKEN);
        if (jwt.getClaim(TokenClaim.PASSWORD_SUBSTRING.name()).asString() == null) {
            throw new SecurityException("Invalid password reset token");
        }
        return jwt;
    }

    private String generateToken(String subject, TokenType tokenType, long expirationTime,
            Map<TokenClaim, Object> additionalClaims) {
        Builder jwtBuilder = JWT.create()
                .withSubject(subject)
                .withClaim(TokenClaim.TOKEN_TYPE.name(), tokenType.name())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date((new Date()).getTime() + expirationTime));

        // Add additional claims
        if (additionalClaims != null) {
            additionalClaims.forEach((claim, value) -> jwtBuilder.withClaim(claim.name(), value.toString()));
        }

        return jwtBuilder.sign(Algorithm.HMAC512(JWT_SECRET));
    }

    private DecodedJWT validateToken(String token, TokenType expectedTokenType) {
        DecodedJWT jwt = validateTokenAndGetDecodedJWT(token);

        // Validate token type
        if (!jwt.getClaim(TokenClaim.TOKEN_TYPE.name()).asString().equals(expectedTokenType.name())) {
            throw new SecurityException("Invalid token type");
        }
        return jwt;
    }

    private DecodedJWT validateTokenAndGetDecodedJWT(String token) {
        try {
            JWTVerifier verifier = JWT.require(getAlgorithm()).build();
            return verifier.verify(token);
        } catch (JWTVerificationException exception) {
            if (exception instanceof TokenExpiredException) {
                throw new SecurityException("Token expired", exception);
            } else {
                throw new SecurityException("Authentication failed, token invalid", exception);
            }
        }
    }

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC512(JWT_SECRET);
    }
}
