package com.hospital.app.dto.auth;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record RefreshTokenRequest(
        @NotNull
        @NotEmpty
        String refreshToken,
        @NotNull
        @NotEmpty
        String accessToken
) {
}
