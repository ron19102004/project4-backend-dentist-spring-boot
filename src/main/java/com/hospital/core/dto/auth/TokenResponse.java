package com.hospital.core.dto.auth;

import com.hospital.core.entities.account.User;
import lombok.Builder;

@Builder
public record TokenResponse(
        String accessToken,
        String refreshToken,
        User user
) {
}
