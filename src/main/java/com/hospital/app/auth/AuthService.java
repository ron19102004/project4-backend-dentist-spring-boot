package com.hospital.app.auth;

import com.hospital.app.dto.auth.RegisterRequest;
import com.hospital.app.entities.account.User;
import com.hospital.app.jwt.JwtCreateTokenDTO;

public interface AuthService {
    User register(final RegisterRequest registerRequest);
    void saveToken(final JwtCreateTokenDTO jwtCreateTokenDTO, final String userAgent);
    void refreshToken(final String accessTokenOld,final JwtCreateTokenDTO jwtCreateTokenDTO, final String userAgent);
    void resetPasswordRequest(final String email);
    void resetPasswordHandle(final String token);
}
