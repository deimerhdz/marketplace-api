package com.taurupro.marketplace.domain.service;

import com.amazonaws.services.cognitoidp.model.UserNotFoundException;
import com.taurupro.marketplace.domain.dto.UserDto;
import com.taurupro.marketplace.persistence.model.UserMain;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        UserDto user = isUUID(identifier)
                ? userService.findByCognitoSub(identifier)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con sub: " + identifier))
                : userService.findByEmail(identifier)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con email: " + identifier));

        return UserMain.build(user);
    }
    private boolean isUUID(String value) {
        try {
            UUID.fromString(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
