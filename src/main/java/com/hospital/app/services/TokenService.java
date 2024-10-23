package com.hospital.app.services;

import com.hospital.app.jwt.JwtCreateTokenDTO;
import org.springframework.security.oauth2.jwt.Jwt;

public interface TokenService {
    void validateAccessToken(String accessToken);

    void saveToken(JwtCreateTokenDTO jwtCreateTokenDTO, boolean isMobile);
    void deleteTokenByAccessToken(String accessToken);
}
