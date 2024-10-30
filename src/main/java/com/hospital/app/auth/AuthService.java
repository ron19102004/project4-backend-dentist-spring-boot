package com.hospital.app.auth;

import com.hospital.app.dto.auth.RegisterRequest;
import com.hospital.app.dto.auth.TokenResponse;
import com.hospital.app.entities.account.User;
import com.hospital.app.jwt.JwtCreateTokenDTO;
import org.springframework.security.core.Authentication;

public interface AuthService {
    User register(final RegisterRequest registerRequest);

    void saveToken(final JwtCreateTokenDTO jwtCreateTokenDTO, final String userAgent);

    void refreshToken(final String accessTokenOld, final JwtCreateTokenDTO jwtCreateTokenDTO, final String userAgent);

    void resetPasswordRequest(final String email);

    void resetPasswordHandle(final String token);

    TokenResponse login(final Authentication authentication, final User user, final String userAgent);

    TokenResponse verifyOTP(final String token, final String otpCode, String userAgent);
    void changeTFAStatus(final User user);
}
