package com.taurupro.marketplace.domain.dto;

public record AuthResponseDto(
        String status,
        String accessToken,
        String session
) {
}
