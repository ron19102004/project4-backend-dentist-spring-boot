package com.hospital.core.dto.specialize;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record SpecializeCreateUpdateRequest(
        @NotNull
        @NotEmpty
        String name,
        @NotNull
        @NotEmpty
        String description
) {
}
