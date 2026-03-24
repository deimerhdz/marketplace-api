package com.taurupro.marketplace.persistence.model;

import com.taurupro.marketplace.domain.dto.UserDto;
import com.taurupro.marketplace.persistence.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class UserMain implements UserDetails {
    private String email;
    private String password;

    private Collection<? extends  GrantedAuthority> authorities;

    public UserMain(String email, Collection<? extends GrantedAuthority> authorities) {
        this.email = email;
        this.authorities = authorities;
    }

    public static UserMain build(UserDto user){
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(user.role().name()));

        return new UserMain(user.email(),authorities);
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
