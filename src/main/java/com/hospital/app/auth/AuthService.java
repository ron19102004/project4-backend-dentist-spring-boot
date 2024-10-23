package com.hospital.app.auth;

import com.hospital.app.dto.auth.RegisterRequest;
import com.hospital.app.entities.account.User;
import com.hospital.app.jwt.JwtCreateTokenDTO;

public interface AuthService {
    User register(RegisterRequest registerRequest);
    void saveToken(JwtCreateTokenDTO jwtCreateTokenDTO, String userAgent);
    void refreshToken(String accessTokenOld,JwtCreateTokenDTO jwtCreateTokenDTO, String userAgent);
}
