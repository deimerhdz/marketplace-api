package com.taurupro.marketplace.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record ResourceDto(
        @NotEmpty @Size(max = 3)  List<MediaFileDto> files
) {
}
