package com.hospital.app.dto.specialize;

import lombok.Builder;

@Builder
public record SpecializeResponse(
         String name,
         String slug,
         String description,
         Long id,
         String createdAt
) {
}
