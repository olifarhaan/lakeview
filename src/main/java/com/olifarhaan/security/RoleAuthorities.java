package com.olifarhaan.security;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.olifarhaan.model.User;

@Component
public class RoleAuthorities {
    private static final Map<User.Role, Set<String>> ROLE_AUTHORITIES = new HashMap<>();

    // TODO: Implement the authorities for robustness
    static {
        // Define authorities for ADMIN role
        final Set<String> adminAuthorities = Set.of();

        // Define authorities for USER role
        final Set<String> userAuthorities = Set.of();

        // Store in map
        ROLE_AUTHORITIES.put(User.Role.ADMIN, adminAuthorities);
        ROLE_AUTHORITIES.put(User.Role.USER, userAuthorities);
    }

    public Set<GrantedAuthority> getAuthorities(User.Role role) {
        Set<GrantedAuthority> authorities = new HashSet<>();

        /*
         * Add the role itself as an authority with ROLE_ prefix
         * 
         * hasRole('ADMIN') looks for authority ROLE_ADMIN
         * hasAuthority('BOOKING_READ') looks for exact match
         * 
         * Why should we use authorities?
         * 
         * This approach allows for a more fine-grained security model. Instead of
         * solely relying on the hasRole() method, which checks for a user's role, we
         * can utilize the hasAuthority() method to verify if a user possesses a
         * specific authority. This distinction enables more precise control over
         * access and actions within the application.
         */
        authorities.add(new SimpleGrantedAuthority(String.format("ROLE_%s", role.name())));

        // Add all mapped authorities for this role
        ROLE_AUTHORITIES.getOrDefault(role, Collections.emptySet())
                .forEach(auth -> authorities.add(new SimpleGrantedAuthority(auth)));

        return authorities;
    }
}