package com.taurupro.marketplace.domain.dto;
import jakarta.validation.constraints.Null;

import java.util.List;

public record CatalogDetailDto(
        String name,
        String slug,
        String numRegister,
        BreedDto breed,
        String birthDate,
        Boolean isFeature,
        String description,
        @Null
        MediaFileDto image,
        @Null
        MediaFileDto video,
        List<MediaFileDto> gallery,
        @Null
        MediaFileDto geneticEvaluation,
        List<StrawCatalogDto> straws

) {
}
