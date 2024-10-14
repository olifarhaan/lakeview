package com.olifarhaan.security;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthContext {
    public static Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        /*
         * AnonymousAuthenticationToken is a subclass of Authentication
         * when some urls are excluded from authentication then spring security will
         * return AnonymousAuthenticationToken (authenticated = true)
         * so we need to check if the authentication is an instance of
         * AnonymousAuthenticationToken
         */

        if (!isAuthenticated(authentication)) {
            throw new SecurityException("No authenticated user found");
        }
        return authentication;
    }

    public static boolean isAuthenticated(Authentication authentication) {
        return authentication != null && !(authentication instanceof AnonymousAuthenticationToken);
    }
}
