package com.hospital.app.mappers;

import com.hospital.app.dto.specialize.SpecializeCreateUpdateRequest;
import com.hospital.app.dto.specialize.SpecializeResponse;
import com.hospital.app.entities.account.Specialize;
import com.hospital.app.utils.HtmlZipUtil;
import com.hospital.app.utils.Slugify;
import com.hospital.app.utils.VietNamTime;
import lombok.experimental.UtilityClass;

import java.io.IOException;


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
