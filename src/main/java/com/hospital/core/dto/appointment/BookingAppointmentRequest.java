package com.hospital.core.dto.appointment;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record BookingAppointmentRequest(
        @NotNull
        LocalDate appointmentDate,
        @NotNull
        String appointmentNote,
        @NotEmpty
        List<Long> services,
        @NotNull
        Long dentistId
) {
}
