package com.hospital.core.dto.appointment;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@AllArgsConstructor
@Data
public class QuantityAppointmentDentistDTO {
    private LocalDate appointmentDate;
    private Long quantity;
}
