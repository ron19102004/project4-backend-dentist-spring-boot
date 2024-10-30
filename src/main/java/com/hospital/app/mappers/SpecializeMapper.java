package com.hospital.app.mappers;

import com.hospital.app.entities.account.Specialize;
import com.hospital.app.utils.HtmlZipUtil;
import com.hospital.app.utils.VietNamTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.IOException;


@Builder
@Getter
@AllArgsConstructor
public class SpecializeMapper {
    private String name;
    private String slug;
    private String description;
    private Long id;
    private String createdAt;

    private SpecializeMapper() {}

    public static SpecializeMapper mapper(Specialize specialize) throws IOException {
        if (specialize == null) return null;
        return SpecializeMapper.builder()
                .createdAt(VietNamTime.toStringFormated(specialize.getCreatedAt().toInstant()))
                .id(specialize.getId())
                .name(specialize.getName())
                .slug(specialize.getSlug())
                .description(HtmlZipUtil.decompress(specialize.getDescription()))
                .build();
    }
}
