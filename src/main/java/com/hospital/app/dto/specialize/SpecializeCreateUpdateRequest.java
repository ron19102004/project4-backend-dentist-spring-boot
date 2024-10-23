package com.hospital.app.dto.specialize;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record SpecializeCreateUpdateRequest(
        @NotNull
        @NotEmpty
        String name
) {
}
