package com.application.backend.configuration;

import com.application.backend.model.Role;
import com.application.backend.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;

public class CustomAuthentication implements Authentication {
    User user;
    public CustomAuthentication(User user){
        this.user = user;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(user.getRole().getRoleName()));
    }

    @Override
    public Object getCredentials() {
        return user.getPassword();
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return user;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

    }

    @Override
    public boolean equals(Object another) {
        return false;
    }

    @Override
    public String toString() {
        return null;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String getName() {
        return null;
    }

    public Role getRole() {return user.getRole();}
}
