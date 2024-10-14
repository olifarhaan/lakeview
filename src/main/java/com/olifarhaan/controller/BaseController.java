package com.olifarhaan.controller;

import org.springframework.beans.factory.annotation.Autowired;

import com.olifarhaan.model.User;
import com.olifarhaan.model.User.Role;
import com.olifarhaan.security.AppUserDetails;
import com.olifarhaan.security.AuthContext;
import com.olifarhaan.service.interfaces.IUserService;

public class BaseController {
    @Autowired
    private IUserService userService;

    public String getLoggedInUserId() {
        return AuthContext.getAuthentication().getName();
    }

    public String getLoggedInUserEmail() {
        return ((AppUserDetails) AuthContext.getAuthentication().getPrincipal()).getEmail();
    }

    public Role getLoggedInUserRole() {
        return Role.valueOf(((AppUserDetails) AuthContext.getAuthentication().getPrincipal()).getAuthorities()
                .stream().findFirst().get().getAuthority());
    }

    public User getLoggedInUser() {
        return userService.findUserById(getLoggedInUserId());
    }
}
