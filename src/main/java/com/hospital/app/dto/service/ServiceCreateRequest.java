package com.hospital.app.dto.service;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ServiceCreateRequest(
        @NotNull
        @NotEmpty
        String name,
        @NotNull
        BigDecimal price,
        @NotNull
        @NotEmpty
        String description
) {
}
