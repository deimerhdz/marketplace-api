package com.taurupro.marketplace.web.controller;

import com.taurupro.marketplace.domain.dto.*;
import com.taurupro.marketplace.domain.service.UserDetailsServiceImpl;
import com.taurupro.marketplace.domain.service.UserService;
import com.taurupro.marketplace.persistence.model.UserMain;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@Validated
public class AuthController {

    private final UserService userService;

    private final UserDetailsServiceImpl userDetailsService;

    public AuthController(UserService userService, UserDetailsServiceImpl userDetailsService) {
        this.userService = userService;
        this.userDetailsService = userDetailsService;
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginDto request) {
        AuthResponseDto response = authenticate(request.email(), request.password());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@Valid @RequestBody SingUpCustomerDto request) {
        userService.signUpCustomer(request);

        AuthResponseDto response = authenticate(request.email(), request.password());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @GetMapping("/refresh")
    public ResponseEntity<AuthResponseDto> refresh(
            @RequestHeader("Authorization") String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String refreshToken = authHeader.substring(7);
        AuthResponseDto response = userService.refreshToken(refreshToken);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserAuthDto> refresh(
            @AuthenticationPrincipal UserMain userMain) {
        UserDto userDto = userService.findByEmail(userMain.getUsername()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));;
        return ResponseEntity.ok(new UserAuthDto(userDto.name(),userDto.lastName(),userDto.role()));
    }

    @PostMapping("/confirm-password")
    public ResponseEntity<AuthResponseDto> confirmPassword(@RequestBody @Valid ConfirmPasswordDto dto) {
        return ResponseEntity.ok(userService.confirmNewPassword(dto));
    }

    private AuthResponseDto authenticate(String email, String password) {
        AuthResponseDto response = userService.signInUser(new LoginDto(email, password));

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return response;
    }
}