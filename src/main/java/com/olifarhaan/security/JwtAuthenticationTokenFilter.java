package com.olifarhaan.security;

import java.io.IOException;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.olifarhaan.security.JwtUtils.TokenClaim;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private AppUserDetailsService userDetailsService;

    @Autowired
    private JwtUtils jwtUtils;

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationTokenFilter.class);
    public static final String[] ALLOWED_PATHS = { "/api/v1/auth/**", "/error",
            "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/api-docs/**", "/api/v1/home/**",
            "/api/v1/cleanup", "/api/v1/room-classes/findByAvailability"
    };

    private boolean isUriAllowed(String uri) {
        logger.debug("Matching uri: {} with allowed paths: {}", uri, Arrays.toString(ALLOWED_PATHS));
        for (String pattern : ALLOWED_PATHS) {
            if (uri.matches(pattern.replace("/**", ".*"))) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain chain) throws ServletException, IOException {
        try {
            if (isUriAllowed(request.getRequestURI())) {
                chain.doFilter(request, response);
                return;
            }

            String authToken = parseJwt(request);

            DecodedJWT decodedJWT = jwtUtils.validateAuthTokenAndGetDecodedJWT(authToken);
            String userId = decodedJWT.getClaim(TokenClaim.USER_ID.name()).asString();
            if (userId == null) {
                throw new BadCredentialsException("The token is invalid");
            }

            UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
            if (userDetails == null || SecurityContextHolder.getContext().getAuthentication() != null) {
                throw new BadCredentialsException("The token is invalid");
            }

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userId, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        } catch (SecurityException e) {
            logger.error("Authentication failed, securityException", e);
            throw new BadCredentialsException(e.getMessage());
        }
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!StringUtils.hasText(headerAuth) || !headerAuth.startsWith(JwtUtils.BEARER_PREFIX)) {
            throw new InsufficientAuthenticationException(
                    "Authorization header is missing or does not have the Bearer prefix");
        }
        return headerAuth.substring(7);
    }
}
