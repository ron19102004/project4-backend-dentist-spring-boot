package com.hospital.core.auth;

import com.hospital.core.dto.auth.RegisterRequest;
import com.hospital.core.dto.auth.TokenResponse;
import com.hospital.core.entities.account.User;
import com.hospital.infrastructure.jwt.JwtCreateTokenDTO;
import org.springframework.security.core.Authentication;

public interface AuthService {
    User register(final RegisterRequest registerRequest);

    void saveToken(final JwtCreateTokenDTO jwtCreateTokenDTO, final String userAgent);

    void refreshToken(final String accessTokenOld, final JwtCreateTokenDTO jwtCreateTokenDTO, final String userAgent);

    void resetPasswordRequest(final String email);

    void resetPasswordHandle(final String token);

    TokenResponse generateLoginToken(final Authentication authentication, final User user, final String userAgent);

    TokenResponse verifyOTP(final String token, final String otpCode, String userAgent);
    void changeTFAStatus(final User user);
}
