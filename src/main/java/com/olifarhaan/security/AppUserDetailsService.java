package com.olifarhaan.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.olifarhaan.model.User;
import com.olifarhaan.service.interfaces.IUserService;

import lombok.RequiredArgsConstructor;

/**
 * @author M. Ali Farhan
 */
@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private final IUserService userService;
    private final RoleAuthorities roleAuthorities;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userService.findUserById(userId);
        return new AppUserDetails(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                roleAuthorities.getAuthorities(user.getRole()));
    }
}
