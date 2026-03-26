package com.taurupro.marketplace.domain.dto;
import java.util.List;

public record CatalogDetailDto(
        String name,
        String slug,
        String stud,
        BreedDto breed,
        String birthDate,
        Boolean isFeature,
        MediaFileDto image,
        MediaFileDto video,
        List<MediaFileDto> gallery,
        String description,
        List<StrawCatalogDto> straws

) {
}
