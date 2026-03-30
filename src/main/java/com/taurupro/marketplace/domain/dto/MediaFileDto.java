package com.taurupro.marketplace.domain.dto;

public record MediaFileDto(
         String key,
         String contentType
) {
    public MediaFileDto {
        key = key != null ? key : "";
        contentType = contentType != null ? contentType : "";
    }
}
