package com.taurupro.marketplace.domain.dto;

public record AuthResponseDto(
        String status,
        String accessToken,
        String refreshToken,
        String session,
        UserAuthDto user
) {
}
