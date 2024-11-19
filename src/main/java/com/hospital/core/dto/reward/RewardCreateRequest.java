package com.hospital.core.dto.reward;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record RewardCreateRequest(
        @NotEmpty
        @NotNull
        Long points,
        @NotEmpty
        @NotNull
        String content,
        @NotNull
        String poster
) {
}
