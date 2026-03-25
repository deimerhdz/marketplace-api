package com.taurupro.marketplace.domain.dto;

import java.util.List;

public record CatalogDto(
        String name,
        String slug,
        Boolean isFeature,
        BreedDto breed,
        MediaFileDto image,
        List<StrawCatalogDto> straws
) {
}
