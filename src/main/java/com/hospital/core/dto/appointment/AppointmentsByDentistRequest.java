package com.hospital.core.dto.appointment;

import com.hospital.core.entities.work.AppointmentStatus;

import java.time.LocalDate;

public record AppointmentsByDentistRequest(
        LocalDate date,
        AppointmentStatus status
) {
}
