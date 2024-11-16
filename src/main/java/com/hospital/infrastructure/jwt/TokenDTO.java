package com.hospital.infrastructure.jwt;
import lombok.Builder;

import java.util.Map;

@Builder
public record TokenDTO(
        String subject,
        Map<String,Object> claims
) {
}
