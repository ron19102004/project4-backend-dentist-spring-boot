package com.hospital.infrastructure.jwt;

import org.springframework.security.oauth2.jwt.Jwt;

public record JwtCreateTokenDTO(
        Jwt jwtAccessToken,
        String refreshToken,
        Long userId
) {
}
