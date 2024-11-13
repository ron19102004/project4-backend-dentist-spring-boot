package com.hospital.app.dto.user_appointment;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;

public record BookingAppointmentRequest(
        @NotNull
        Date appointmentDate,
        @NotNull
        String appointmentNote,
        @NotEmpty
        List<Long> services,
        @NotNull
        Long dentistId
) {
}
