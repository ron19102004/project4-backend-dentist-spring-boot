package com.hospital.core.dto.dental_record;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record DentalRecordUpdate(
        @NotNull
        String diagnosis,
        @NotNull
        String treatment,
        @NotNull
        String notes,
        @NotNull
        Long dentistId
) {
}
