package com.hospital.app.dto;

public record TokenDTO(
        String accessToken,
        String refreshToken,
        Long userId
) {
}
