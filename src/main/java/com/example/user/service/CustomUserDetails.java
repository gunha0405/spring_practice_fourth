package com.example.user.service;

import com.example.user.model.SiteUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public class CustomUserDetails implements UserDetails, OAuth2User {
    private final SiteUser user;
    private final Collection<? extends GrantedAuthority> authorities;
    private final Map<String, Object> attributes;

    public CustomUserDetails(SiteUser user,
                             Collection<? extends GrantedAuthority> authorities,
                             Map<String, Object> attributes) {
        this.user = user;
        this.authorities = authorities;
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return user.getUsername();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

    // SiteUser 직접 꺼내쓰고 싶을 때
    public SiteUser getUser() {
        return user;
    }
}
