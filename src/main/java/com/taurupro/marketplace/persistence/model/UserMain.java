package com.taurupro.marketplace.persistence.model;

import com.taurupro.marketplace.domain.dto.UserDto;
import com.taurupro.marketplace.persistence.entity.UserEntity;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class UserMain implements UserDetails {
    @Getter
    private final UUID id;
    private String email;
    private String password;
    @Getter
    private final UUID supplierId;

    private Collection<? extends  GrantedAuthority> authorities;

    public UserMain(UUID id,String email, UUID supplierId,Collection<? extends GrantedAuthority> authorities) {
        this.id=id;
        this.email = email;
        this.supplierId=supplierId;
        this.authorities = authorities;
    }

    public static UserMain build(UserDto user){
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(user.role().name()));
        UUID supplierId = user.supplier() != null
                ? user.supplier().id()
                : null;
        return new UserMain(user.id(),user.email(),supplierId,authorities);
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
