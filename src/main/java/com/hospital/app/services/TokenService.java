package com.hospital.app.services;

import com.hospital.app.jwt.JwtCreateTokenDTO;
import org.springframework.security.oauth2.jwt.Jwt;

public interface TokenService {
    boolean validateAccessToken(final String accessToken);

    void saveToken(final JwtCreateTokenDTO jwtCreateTokenDTO, final boolean isMobile);

    void deleteTokenByAccessToken(final String accessToken);
}
