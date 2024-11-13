package com.hospital.app.dto.appointment;

import com.hospital.app.entities.work.AppointmentStatus;

import java.time.LocalDate;
import java.util.Date;

public record AppointmentsByDentistRequest(
        LocalDate date,
        AppointmentStatus status
) {
}
