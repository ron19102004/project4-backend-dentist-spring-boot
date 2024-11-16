package com.hospital.core.services;

import com.hospital.infrastructure.jwt.JwtCreateTokenDTO;

public interface TokenService {
    boolean validateAccessToken(final String accessToken);

    void saveToken(final JwtCreateTokenDTO jwtCreateTokenDTO, final boolean isMobile);

    void deleteTokenByAccessToken(final String accessToken);
}
