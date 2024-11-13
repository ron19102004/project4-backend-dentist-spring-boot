package com.hospital.app.dto.appointment;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@AllArgsConstructor
@Data
public class QuantityAppointmentDentistDTO {
    private LocalDate appointmentDate;
    private Long quantity;
}
