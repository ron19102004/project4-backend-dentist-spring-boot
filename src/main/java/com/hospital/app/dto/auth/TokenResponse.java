package com.hospital.app.dto.auth;

import com.hospital.app.entities.account.User;
import lombok.Builder;

@Builder
public record TokenResponse(
        String accessToken,
        String refreshToken,
        User user
) {
}
