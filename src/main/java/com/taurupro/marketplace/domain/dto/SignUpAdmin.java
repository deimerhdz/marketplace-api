package com.taurupro.marketplace.domain.dto;

public record SignUpAdmin(
        String email,
        String name,
        String lastName,
        String password
) {}
