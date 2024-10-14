package com.olifarhaan.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.olifarhaan.security.AuthContext;

public class ApplicationAuditorAware implements AuditorAware<String> {

    @Override
    public @NonNull Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!AuthContext.isAuthenticated(authentication)) {
            return Optional.of("SYSTEM");
        }
        return Optional.of(authentication.getName());
    }
}