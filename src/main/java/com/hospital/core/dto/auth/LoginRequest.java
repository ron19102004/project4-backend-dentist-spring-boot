package com.hospital.core.dto.auth;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record LoginRequest(
        @NotNull
        @NotEmpty
        String username,
        @NotNull
        @Length(min = 8, max = 20)
        @NotEmpty
        String password
) {
}
