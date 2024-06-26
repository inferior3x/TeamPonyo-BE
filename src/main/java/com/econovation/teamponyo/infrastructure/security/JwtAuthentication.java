package com.econovation.teamponyo.infrastructure.security;

import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public class JwtAuthentication implements Authentication {
    private final Long userId;
    private final Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public Long getPrincipal() {
        return this.userId;
    }

    @Override
    public String getName(){
        return this.userId.toString();
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public Object getCredentials(){
        throw new UnsupportedOperationException();
    }

    @Override
    public Object getDetails(){
        throw new UnsupportedOperationException();
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated){
        throw new UnsupportedOperationException();
    }
}
