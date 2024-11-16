package com.hospital.core.mappers;

import com.hospital.core.dto.specialize.SpecializeCreateUpdateRequest;
import com.hospital.core.dto.specialize.SpecializeResponse;
import com.hospital.core.entities.account.Specialize;
import com.hospital.infrastructure.utils.Slugify;
import com.hospital.infrastructure.utils.VietNamTime;
import lombok.experimental.UtilityClass;


@UtilityClass
public class SpecializeMapper {
    public SpecializeResponse toSpecializeResponse(Specialize specialize) {
        if (specialize == null) return null;
        SpecializeResponse.SpecializeResponseBuilder responseBuilder = SpecializeResponse.builder()
                .createdAt(VietNamTime.toStringFormated(specialize.getCreatedAt().toInstant()))
                .id(specialize.getId())
                .name(specialize.getName())
                .slug(specialize.getSlug())
                .description(specialize.getDescription());
        return responseBuilder.build();
    }

    public Specialize toSpecialize(SpecializeCreateUpdateRequest specializeCreateUpdateRequest) {
        return Specialize.builder()
                .name(specializeCreateUpdateRequest.name())
                .slug(Slugify.toSlug(specializeCreateUpdateRequest.name()))
                .description(specializeCreateUpdateRequest.description())
                .build();
    }
}
